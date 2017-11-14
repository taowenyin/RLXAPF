package cn.edu.siso.rlxapf;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.siso.rlxapf.bean.CityBean;
import cn.edu.siso.rlxapf.bean.CountyBean;
import cn.edu.siso.rlxapf.bean.DeviceBean;
import cn.edu.siso.rlxapf.bean.FilterBean;
import cn.edu.siso.rlxapf.bean.ProvinceBean;
import cn.edu.siso.rlxapf.bean.UserBean;
import cn.edu.siso.rlxapf.config.HTTPConfig;
import cn.edu.siso.rlxapf.config.TCPConfig;
import cn.edu.siso.rlxapf.dialog.ConnectDialogFragment;
import cn.edu.siso.rlxapf.recycle.AbstractItem;
import cn.edu.siso.rlxapf.recycle.filter.DeviceFilterRecycleAdapter;
import cn.edu.siso.rlxapf.recycle.filter.FilterExpandableHeaderItem;
import cn.edu.siso.rlxapf.recycle.filter.FilterExpandableSubHeadItem;
import cn.edu.siso.rlxapf.recycle.filter.FilterExpandableSubItem;
import cn.edu.siso.rlxapf.util.http.OkHttpClientManager;
import cn.edu.siso.rlxapf.util.tcp.TcpClientManager;
import eu.davidea.fastscroller.FastScroller;
import eu.davidea.flexibleadapter.common.SmoothScrollGridLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;

public class DeviceListActivity extends AppCompatActivity implements
        DeviceListRecyclerAdapter.OnItemClickListener,
        DeviceListRecyclerAdapter.OnOperatorClickListener,
        DrawerLayout.DrawerListener, OnItemCheckedChangedListener {

    private enum CurrOperate {NO_OPERATE, ENTER_PARAMS, ENTER_REAL_DATA};

    private RecyclerView deviceListView  = null;
    private RecyclerView searchFilterView = null;

    private DeviceListRecyclerAdapter deviceAdapter = null;
    private List<DeviceBean> deviceData = null;
    private List<DeviceBean> deviceFilterData = null;

    private DeviceFilterRecycleAdapter filterAdapter = null;
    private List<AbstractFlexibleItem> filterData = null;

    private List<String> searchWhere = null;

    private PopupBottomMenu operateMenu = null;
    private WindowManager.LayoutParams wLP = null;
    private ConnectDialogFragment dialogFragment = null;
    private SwipeRefreshLayout swipeRefresh = null;

    private DrawerLayout deviceListDrawer = null;
    private LinearLayout deviceListSearchLayout = null;

    private Handler httpHandler = null;
    private OkHttpClientManager httpManager = null;
    private boolean isTimeout = false;

    private TcpClientManager tcpClientManager = null;
    private Handler tcpHandler = null;

    private UserBean userBean = null;

    // 当前的操作
    private CurrOperate currentOperate = CurrOperate.NO_OPERATE;

    private int currentDevicePosition = -1;

    public static final String USER_KEY = "user";
    public static final String DATA_KEY = "data";
    public static final String POSITION_KEY = "position";

    public static final String TAG = "DeviceListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        userBean = JSON.parseObject(
                getIntent().getStringExtra(LoginActivity.USER_DATA_KEY),
                UserBean.class);

        deviceListDrawer = (DrawerLayout) findViewById(R.id.device_list_drawer);
        deviceListDrawer.addDrawerListener(this);
        deviceListSearchLayout = (LinearLayout) findViewById(R.id.device_list_search);

        ImageButton toolbarBack = (ImageButton) findViewById(R.id.toolbar_back);
        ImageButton toolbarSearch = (ImageButton) findViewById(R.id.toolbar_search);
        Button filterSearchOk = (Button) findViewById(R.id.device_search_ok);
        Button filterSearchCancel = (Button) findViewById(R.id.device_search_reset);
        deviceListView  = (RecyclerView) findViewById(R.id.device_list_view);
        searchFilterView = (RecyclerView) findViewById(R.id.device_search_filter);

        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        dialogFragment = new ConnectDialogFragment(); // 初始化通信对话框对象

        deviceData = new ArrayList<DeviceBean>();
        deviceFilterData = new ArrayList<DeviceBean>();
        filterData = new ArrayList<AbstractFlexibleItem>();
        searchWhere = new ArrayList<String>();

        httpHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                Bundle data = msg.getData();
                String resultType = data.getString(OkHttpClientManager.HTTP_RESPONSE_TYPE);
                String resultData = data.getString(OkHttpClientManager.HTTP_RESPONSE_DATA);

                if (dialogFragment.getShowsDialog()) {
                    dialogFragment.dismiss();
                }

                // 清空设备数据
                deviceData.clear();
                // 清空筛选设备数据
                deviceFilterData.clear();
                // 清空筛选数据
                filterData.clear();
                // 添加筛选信息
                FilterBean filterBean = null;

                if (!TextUtils.isEmpty(resultData) && StringUtils.isNumeric(resultData)) {
                    String badMsg = "";
                    int resCode = Integer.parseInt(resultData);
                    switch (resCode) {
                        case HTTPConfig.DeviceListError.MOBILE_ID_NO_SET:
                            badMsg = getResources().getString(R.string.device_list_error_mobile_id_no_set);
                            break;
                        case HTTPConfig.DeviceListError.SESSION_NO_SET:
                            badMsg = getResources().getString(R.string.device_list_error_session_no_set);

                            // 清空账户信息
                            SharedPreferences accountPref = getSharedPreferences(
                                    getResources().getString(R.string.account_pref_name), MODE_PRIVATE);
                            SharedPreferences.Editor editor = accountPref.edit();
                            editor.clear();
                            SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);

                            Intent intent = new Intent(DeviceListActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                            break;
                        case HTTPConfig.DeviceListError.SESSION_ERROR:
                            badMsg = getResources().getString(R.string.device_list_error_session_error);
                            break;
                        case HTTPConfig.DeviceListError.NO_DATA:
                            badMsg = getResources().getString(R.string.device_list_error_no_data);
                            break;
                    }

                    Toast toast = new ConnectToast(
                            getApplicationContext(),
                            ConnectToast.ConnectRes.BAD, badMsg, Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    List<DeviceBean> devices = JSON.parseArray(resultData, DeviceBean.class);
                    filterBean = new FilterBean();

                    // 添加设备列表
                    for (int i = 0; i < devices.size(); i++) {
                        DeviceBean deviceBean = devices.get(i);

                        String province = deviceBean.getProvince();
                        String city = deviceBean.getCity();
                        String county = deviceBean.getCounty();
                        String name = deviceBean.getName();
                        String type = deviceBean.getDeviceType();
                        String gprs = deviceBean.getGPSDeviceNo();
                        String no = deviceBean.getDeviceNo();

                        // 添加筛选地址
                        ProvinceBean provinceBean = filterBean.getProvince(province);
                        CityBean cityBean = provinceBean.getCity(city);
                        CountyBean countyBean = cityBean.getCounty(county);

                        // 添加筛选类型
                        filterBean.addName(name);
                        filterBean.addType(type);
                        filterBean.addGprs(gprs);
                        filterBean.addNo(no);

                        deviceData.add(deviceBean);
                    }

                    Device_Array_COPY(deviceData, deviceFilterData);

                    deviceAdapter.notifyDataSetChanged();
                }

                if (filterBean != null) {
                    // 添加筛选地理列表
                    List<ProvinceBean> provinceBeanList = filterBean.getProvinces();
                    for (int i = 0; i < provinceBeanList.size(); i++) {
                        ProvinceBean provinceBean = provinceBeanList.get(i);

                        FilterExpandableHeaderItem headerItem = new FilterExpandableHeaderItem(provinceBean.getName(), AbstractItem.ITEM_TYPE.CATEGORY, getApplicationContext());
                        headerItem.setTitle(provinceBean.getName());

                        List<CityBean> cityBeanList = provinceBean.getCities();
                        for (int j = 0; j < cityBeanList.size(); j++) {
                            CityBean cityBean = cityBeanList.get(j);

                            FilterExpandableSubHeadItem subHeadItem = new FilterExpandableSubHeadItem(headerItem.getId() + "-" + cityBean.getName(),
                                    AbstractItem.ITEM_TYPE.LOCAL, getApplicationContext());
                            subHeadItem.setTitle(cityBean.getName());

                            List<CountyBean> countyBeanList = cityBean.getCounties();
                            for (int k = 0; k < countyBeanList.size(); k++) {
                                CountyBean countyBean = countyBeanList.get(k);

                                FilterExpandableSubItem subItem = new FilterExpandableSubItem(subHeadItem.getId() + "-" + countyBean.getName(),
                                        AbstractItem.ITEM_TYPE.CATEGORY, getApplicationContext());
                                subItem.setTitle(countyBean.getName());

                                subHeadItem.addSubItem(subItem);
                            }

                            headerItem.addSubItem(subHeadItem);
                        }

                        filterData.add(headerItem);
                    }

                    // 添加筛选设备类型列表
                    FilterExpandableHeaderItem nameItem = new FilterExpandableHeaderItem("客户名称",
                            AbstractItem.ITEM_TYPE.CATEGORY, getApplicationContext());
                    nameItem.setTitle("客户名称");
                    for (int i = 0; i < filterBean.getDevicesName().size(); i++) {
                        FilterExpandableSubHeadItem subItem = new FilterExpandableSubHeadItem(
                                nameItem.getId() + "-" + filterBean.getDevicesName().get(i),
                                AbstractItem.ITEM_TYPE.CATEGORY, getApplicationContext());
                        subItem.setTitle(filterBean.getDevicesName().get(i));
                        nameItem.addSubItem(subItem);
                    }
                    filterData.add(nameItem);

                    // 添加筛选设备类型列表
                    FilterExpandableHeaderItem typeItem = new FilterExpandableHeaderItem("设备类型",
                            AbstractItem.ITEM_TYPE.CATEGORY, getApplicationContext());
                    typeItem.setTitle("设备类型");
                    for (int i = 0; i < filterBean.getDevicesType().size(); i++) {
                        FilterExpandableSubHeadItem subItem = new FilterExpandableSubHeadItem(
                                typeItem.getId() + "-" + filterBean.getDevicesType().get(i),
                                AbstractItem.ITEM_TYPE.CATEGORY, getApplicationContext());
                        subItem.setTitle(filterBean.getDevicesType().get(i));
                        typeItem.addSubItem(subItem);
                    }
                    filterData.add(typeItem);

                    // 添加筛选设备GPRS列表
                    FilterExpandableHeaderItem gprsItem = new FilterExpandableHeaderItem("GPRS模块号",
                            AbstractItem.ITEM_TYPE.CATEGORY, getApplicationContext());
                    gprsItem.setTitle("GPRS模块号");
                    for (int i = 0; i < filterBean.getDevicesGprs().size(); i++) {
                        FilterExpandableSubHeadItem subItem = new FilterExpandableSubHeadItem(
                                gprsItem.getId() + "-" + filterBean.getDevicesGprs().get(i),
                                AbstractItem.ITEM_TYPE.CATEGORY, getApplicationContext());
                        subItem.setTitle(filterBean.getDevicesGprs().get(i));
                        gprsItem.addSubItem(subItem);
                    }
                    filterData.add(gprsItem);

                    // 添加筛选设备号列表
                    FilterExpandableHeaderItem deviceItem = new FilterExpandableHeaderItem("设备号",
                            AbstractItem.ITEM_TYPE.CATEGORY, getApplicationContext());
                    deviceItem.setTitle("设备号");
                    for (int i = 0; i < filterBean.getDevicesNo().size(); i++) {
                        FilterExpandableSubHeadItem subItem = new FilterExpandableSubHeadItem(
                                deviceItem.getId() + "-" + filterBean.getDevicesNo().get(i),
                                AbstractItem.ITEM_TYPE.CATEGORY, getApplicationContext());

                        subItem.setTitle(filterBean.getDevicesNo().get(i));
                        deviceItem.addSubItem(subItem);
                    }
                    filterData.add(deviceItem);

                    filterAdapter.updateDataSet(filterData);
                }

                // 关闭读取进度
                if (swipeRefresh.isRefreshing()) {

                    swipeRefresh.post(new Runnable(){
                        @Override
                        public void run() {
                            swipeRefresh.setRefreshing(false);
                        }
                    });
                }
            }
        };

        tcpHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if (dialogFragment.getShowsDialog()) {
                    dialogFragment.dismiss();
                }

                Bundle data = msg.getData();
                String tcpOperateType = data.getString(TcpClientManager.KEY_TCP_OPERATE_TYPE);
                String tcpResType = data.getString(TcpClientManager.KEY_TCP_RES_TYPE);

                if (tcpOperateType.equals(TcpClientManager.TcpOperateType.CONNECT)) {
                    // 如果超时，则禁止更新数据
                    if (!isTimeout) {
                        if (tcpResType.equals(TcpClientManager.TcpResType.SUCCESS)) {
                            if (currentOperate == CurrOperate.ENTER_REAL_DATA) {
                                Intent intent = new Intent(DeviceListActivity.this, MainActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString(DATA_KEY, JSON.toJSONString(
                                        deviceData, SerializerFeature.WriteMapNullValue));
                                bundle.putString(USER_KEY, JSON.toJSONString(
                                        userBean, SerializerFeature.WriteMapNullValue));
                                bundle.putInt(POSITION_KEY, currentDevicePosition);
                                intent.putExtras(bundle);

                                currentOperate = CurrOperate.NO_OPERATE;
                                startActivity(intent);
                            }
                            if (currentOperate == CurrOperate.ENTER_PARAMS) {
                                Intent intent = new Intent(DeviceListActivity.this, ParamActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString(DATA_KEY, JSON.toJSONString(
                                        deviceData, SerializerFeature.WriteMapNullValue));
                                bundle.putInt(POSITION_KEY, currentDevicePosition);
                                intent.putExtras(bundle);

                                currentOperate = CurrOperate.NO_OPERATE;
                                startActivity(intent);
                            }
                        }
                        if (tcpResType.equals(TcpClientManager.TcpResType.ERROR)) {
                            String tcpResCode = data.getString(TcpClientManager.KEY_TCP_RES_CODE);
                            String tcpResData = data.getString(TcpClientManager.KEY_TCP_RES_DATA);

                            ConnectToast toast = new ConnectToast(getApplicationContext(),
                                    ConnectToast.ConnectRes.BAD,
                                    tcpResCode + " : " + tcpResData,
                                    Toast.LENGTH_LONG);
                        }
                        if (tcpResType.equals(TcpClientManager.TcpResType.CLOSE)) {
                            ConnectToast toast = new ConnectToast(getApplicationContext(),
                                    ConnectToast.ConnectRes.BAD,
                                    getResources().getString(R.string.tcp_connect_close),
                                    Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                    if (tcpResType.equals(TcpClientManager.TcpResType.TIMEOUT)) {
                        // 标记当前为超时状态
                        isTimeout = true;

                        ConnectToast toast = new ConnectToast(getApplicationContext(),
                                ConnectToast.ConnectRes.BAD,
                                getResources().getString(R.string.tcp_connect_time_out),
                                Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            }
        };

        // 初始化弹出框数据
        wLP = getWindow().getAttributes();
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        String[] operatePopupData = getResources().getStringArray(
                R.array.device_operate_little_popup_window);
        for (int i = 0; i < operatePopupData.length; i++) {
            Map<String, String> item = new HashMap<String, String>();
            item.put(PopupBottomMenu.TITLE_KEY, operatePopupData[i]);
            data.add(item);
        }
        operateMenu = new PopupBottomMenu(getApplicationContext(),
                data,
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        wLP.alpha = 1f;
                        DeviceListActivity.this.getWindow().setAttributes(wLP);
                        operateMenu.dismiss();

                        DeviceBean deviceBean = deviceData.get(currentDevicePosition);
                        switch (position) {
                            case 0:
                                if (tcpClientManager.getConnectStatus()) {
                                    Intent intent = new Intent(DeviceListActivity.this, ParamActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString(DATA_KEY, JSON.toJSONString(
                                            deviceData, SerializerFeature.WriteMapNullValue));
                                    bundle.putInt(POSITION_KEY, currentDevicePosition);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                } else {
                                    isTimeout = false; // 清空超时，允许进行TCP操作

                                    tcpClientManager.connect(getApplicationContext(),
                                            deviceBean.getGPSDeviceNo(),
                                            TCPConfig.DEFAULT_DEVICE_PWD, tcpHandler);
                                    dialogFragment.show(getSupportFragmentManager(), DeviceListActivity.class.getName());
                                    // 即将进入参数配置页面
                                    currentOperate = CurrOperate.ENTER_PARAMS;
                                }
                                break;
                        }
                    }
                },
                new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        wLP.alpha = 1f;
                        DeviceListActivity.this.getWindow().setAttributes(wLP);
                        operateMenu.dismiss();
                    }
                },
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        wLP.alpha = 1f;
                        DeviceListActivity.this.getWindow().setAttributes(wLP);
                        operateMenu.dismiss();
                    }
                });

        // 初始化列表数据
        final LinearLayoutManager deviceLayoutManager = new LinearLayoutManager(
                getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        deviceAdapter = new DeviceListRecyclerAdapter(getApplicationContext(), deviceFilterData);
        deviceAdapter.setOnItemClickListener(this);
        deviceAdapter.setOnOperatorClickListener(this);
        deviceListView.setLayoutManager(deviceLayoutManager);
        deviceListView.setAdapter(deviceAdapter);

        // 初始化筛选列表
        filterAdapter = new DeviceFilterRecycleAdapter(filterData, this);
        filterAdapter.expandItemsAtStartUp();
        GridLayoutManager gridLayoutManager = new SmoothScrollGridLayoutManager(getApplicationContext(), 4);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                AbstractItem item = (AbstractItem) filterAdapter.getItem(position);
                switch (filterAdapter.getItemViewType(position)) {
                    case R.layout.filter_expandable_header_item:
                    case R.layout.filter_expandable_local_item:
                        return 4;
                    case R.layout.filter_expandable_category_item:
                        return 2;
                    case R.layout.filter_expandable_sub_item:
                        return 2;
                    default:
                        return 1;
                }
            }
        });
        searchFilterView.setLayoutManager(gridLayoutManager);
        searchFilterView.setAdapter(filterAdapter);
        FastScroller fastScroller = (FastScroller) findViewById(R.id.fast_scroller);
        filterAdapter.setFastScroller(fastScroller);
        filterAdapter.setOnItemClickListener(this);

        // 标题栏点击事件
        toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 清空用户数据
                SharedPreferences.Editor editor = getSharedPreferences(
                        getResources().getString(R.string.account_pref_name),
                        MODE_PRIVATE).edit();
                editor.clear();
                SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);

                Intent intent = new Intent(DeviceListActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        toolbarSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deviceListDrawer.isDrawerOpen(deviceListSearchLayout)) {
                    deviceListDrawer.closeDrawer(deviceListSearchLayout, true);
                } else {
                    deviceListDrawer.openDrawer(deviceListSearchLayout, true);
                }
            }
        });

        filterSearchOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deviceListDrawer.closeDrawer(deviceListSearchLayout, true);

                // 获取检索条件
                List<String> customWhereList = new ArrayList<String>();
                List<String> typeWhereList = new ArrayList<String>();
                List<String> gprsWhereList = new ArrayList<String>();
                List<String> deviceWhereList = new ArrayList<String>();
                List<String> localWhereList = new ArrayList<String>();

                for (String where : searchWhere) {
                    List<String> whereItem = Arrays.asList(where.split("-"));

                    if (whereItem.get(0).equals("客户名称")) {
                        customWhereList.add(whereItem.get(1));
                    } else if (whereItem.get(0).equals("设备类型")) {
                        typeWhereList.add(whereItem.get(1));
                    } else if (whereItem.get(0).equals("GPRS模块号")) {
                        gprsWhereList.add(whereItem.get(1));
                    } else if (whereItem.get(0).equals("设备号")) {
                        deviceWhereList.add(whereItem.get(1));
                    } else {
                        localWhereList.add(whereItem.get(2));
                    }
                }

                deviceFilterData.clear();
                for (int i = 0; i < deviceData.size(); i++) {
                    DeviceBean bean = deviceData.get(i);

                    if (customWhereList.size() > 0 && !isCustomWhere(bean, customWhereList)) {
                        continue;
                    }

                    if (typeWhereList.size() > 0 && !isTypeWhere(bean, typeWhereList)) {
                        continue;
                    }

                    if (gprsWhereList.size() > 0 && !isGprsWhere(bean, gprsWhereList)) {
                        continue;
                    }

                    if (deviceWhereList.size() > 0 && !isDeviceWhere(bean, deviceWhereList)) {
                        continue;
                    }

                    if (localWhereList.size() > 0 && !isLocalWhere(bean, localWhereList)) {
                        continue;
                    }

                    deviceFilterData.add(bean);
                }

                deviceAdapter.notifyDataSetChanged();
//                searchWhere.clear();
            }
        });

        filterSearchCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deviceListDrawer.closeDrawer(deviceListSearchLayout, true);
                searchWhere.clear();

                // 取消所有选项，并折叠列表
                for (int i = 0; i < filterAdapter.getItemCount(); i++) {
                    AbstractItem item = (AbstractItem) filterAdapter.getItem(i);
                    switch (filterAdapter.getItemViewType(i)) {
                        case R.layout.filter_expandable_category_item:
                            FilterExpandableSubHeadItem.FilterExpandableSubHeadViewHolder subHeadviewHolder =
                                    (FilterExpandableSubHeadItem.FilterExpandableSubHeadViewHolder) searchFilterView.getChildViewHolder(searchFilterView.getChildAt(i));
                            subHeadviewHolder.filterCB.setChecked(false);
                            break;
                        case R.layout.filter_expandable_sub_item:
                            FilterExpandableSubItem.FilterExpandableSubViewHolder subViewHolder =
                                    (FilterExpandableSubItem.FilterExpandableSubViewHolder) searchFilterView.getChildViewHolder(searchFilterView.getChildAt(i));
                            subViewHolder.filterCB.setChecked(false);
                            break;
                    }
                }

                // 折叠所有
                filterAdapter.collapseAll();

                // 显示所有内容
                Device_Array_COPY(deviceData, deviceFilterData);
                deviceAdapter.notifyDataSetChanged();
            }
        });

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobileid", userBean.getMobileId());
                params.put("account", userBean.getAccount());
                httpManager.httpStrGetAsyn(HTTPConfig.API_URL_QUERY_DEVICE, params, httpHandler);
            }
        });

        // 获取设备列表
        RLXApplication application = (RLXApplication) getApplication();
        httpManager = application.getHttpManager();
        Map<String, String> params = new HashMap<String, String>();
        params.put("mobileid", userBean.getMobileId());
        params.put("account", userBean.getAccount());

//        httpManager.httpStrGetAsyn(HTTPConfig.API_URL_QUERY_DEVICE, params, httpHandler);
        dialogFragment.show(getSupportFragmentManager(), DeviceListActivity.class.getName());

        // 初始化TCP连接对象
        tcpClientManager = TcpClientManager.getInstance();
    }

    public static int Device_Array_COPY(List<DeviceBean> src, List<DeviceBean> dest) {
        if (dest != null && src != null) {
            dest.clear();

            for (int i = 0; i < src.size(); i++) {
                dest.add(src.get(i));
            }

            return 0;
        }

        return 1;
    }

    // 检查客户名是否存在
    public static boolean isCustomWhere(DeviceBean bean, List<String> whereList) {
        for (String where : whereList) {
            if (bean.getName().equals(where)) {
                return true;
            }
        }

        return false;
    }

    // 检查设备类型是否存在
    public static boolean isTypeWhere(DeviceBean bean, List<String> whereList) {
        for (String where : whereList) {
            if (bean.getDeviceType().equals(where)) {
                return true;
            }
        }

        return false;
    }

    // 检查GPRS模块号是否存在
    public static boolean isGprsWhere(DeviceBean bean, List<String> whereList) {
        for (String where : whereList) {
            if (bean.getGPSDeviceNo().equals(where)) {
                return true;
            }
        }

        return false;
    }

    // 检查设备号是否存在
    public static boolean isDeviceWhere(DeviceBean bean, List<String> whereList) {
        for (String where : whereList) {
            if (bean.getDeviceNo().equals(where)) {
                return true;
            }
        }

        return false;
    }

    // 检查位置是否存在
    public static boolean isLocalWhere(DeviceBean bean, List<String> whereList) {
        for (String where : whereList) {
            if (bean.getCounty().equals(where)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void onItemClick(View view, int position) {
        currentDevicePosition = position;

        if (tcpClientManager.getConnectStatus()) {
            Log.i(TAG, "设备已经连接，进入监控设备");
            Intent intent = new Intent(DeviceListActivity.this, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(DATA_KEY, JSON.toJSONString(
                    deviceFilterData, SerializerFeature.WriteMapNullValue));
            bundle.putString(USER_KEY, JSON.toJSONString(
                    userBean, SerializerFeature.WriteMapNullValue));
            bundle.putInt(POSITION_KEY, currentDevicePosition);
            intent.putExtras(bundle);
            startActivity(intent);
        } else {
            isTimeout = false; // 清空超时，允许进行TCP操作

            tcpClientManager.connect(getApplicationContext(),
                    deviceFilterData.get(position).getGPSDeviceNo(),
                    TCPConfig.DEFAULT_DEVICE_PWD, tcpHandler);
            dialogFragment.show(getSupportFragmentManager(), DeviceListActivity.class.getName());

            // 即将进入参数配置页面
            currentOperate = CurrOperate.ENTER_REAL_DATA;
        }
    }

    @Override
    public void onOperatorClick(View view, int position) {
        currentDevicePosition = position;

        wLP.alpha = 0.5f;
        DeviceListActivity.this.getWindow().setAttributes(wLP);
        operateMenu.showAtLocation(
                DeviceListActivity.this.findViewById(R.id.device_list_main),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,
                0, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();

        swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(true);

                Map<String, String> params = new HashMap<String, String>();
                params.put("mobileid", userBean.getMobileId());
                params.put("account", userBean.getAccount());
                httpManager.httpStrGetAsyn(HTTPConfig.API_URL_QUERY_DEVICE, params, httpHandler);
            }
        });
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
//        Log.i(TAG, "onDrawerSlide");
    }

    @Override
    public void onDrawerOpened(View drawerView) {
//        Log.i(TAG, "onDrawerOpened");
    }

    @Override
    public void onDrawerClosed(View drawerView) {
//        Log.i(TAG, "onDrawerClosed");
    }

    @Override
    public void onDrawerStateChanged(int newState) {
//        Log.i(TAG, "onDrawerStateChanged");
    }

    @Override
    public void onItemCheckedChanged(CompoundButton buttonView, String where) {
        if (buttonView.isChecked()) {
            if (!searchWhere.contains(where)) {
                searchWhere.add(where);
            }
        } else {
            if (searchWhere.contains(where)) {
                searchWhere.remove(where);
            }
        }
    }
}
