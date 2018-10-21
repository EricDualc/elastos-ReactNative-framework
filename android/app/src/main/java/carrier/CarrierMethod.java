package carrier;


import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableMap;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import android.widget.Toast;
import java.io.File;

import org.elastos.carrier.*;
import org.elastos.carrier.exceptions.CarrierException;
import org.json.JSONObject;


public class CarrierMethod extends ReactContextBaseJavaModule {

    public static final String ok = "ok";

    private HashMap<String, RN_CARRIER> ALL_MAP = new HashMap<String, RN_CARRIER>();
    private Util util;


    public CarrierMethod(ReactApplicationContext reactContext) {
        super(reactContext);

        util = Util.singleton();
    }

    @ReactMethod
    public void test() {
        Toast.makeText(getReactApplicationContext(), "test toast", Toast.LENGTH_LONG).show();
    }

    @Override
    public String getName() {
        return "CarrierPlugin";
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();

        return constants;
    }


    @ReactMethod
    public static void getVersion(Callback callback){
        String version = Carrier.getVersion();
        callback.invoke(null, version);
    }

    @ReactMethod
    public static void isValidAddress(String address, Callback cb){
        Boolean f = Carrier.isValidAddress(address);
        cb.invoke(null, f);
    }

    @ReactMethod
    public void createObject(ReadableMap config, Callback cb){
        String name = config.getString("name");

        RN_CARRIER _rn = new RN_CARRIER(config);
        ALL_MAP.put(name, _rn);

        cb.invoke(null, ok);
    }

    @ReactMethod
    public void getAddress(String name, Callback cb){
        Carrier _carrier = getInstanceByName(name);

        try{
            cb.invoke(null, _carrier.getAddress());
        }catch(CarrierException e){
            util.error("[getAddress] "+e.toString());
            cb.invoke(e.toString(), null);
        }
    }

    @ReactMethod
    public void getSelfInfo(String name, Callback cb){
        Carrier _carrier = getInstanceByName(name);
        RN_CARRIER _rn = getByName(name);

        try{
            RN_UserInfo info = new RN_UserInfo(_carrier.getSelfInfo());
            cb.invoke(null, info.toJS());
        }catch(CarrierException e){
            util.error("[getSelfInfo] "+e.toString());
            cb.invoke(e.toString(), null);
        }
    }

    @ReactMethod
    public void setSelfInfo(String name, ReadableMap info, Callback cb){
        HashMap map = info.toHashMap();
        Carrier _carrier = getInstanceByName(name);

        try{
            RN_UserInfo new_info = new RN_UserInfo(_carrier.getSelfInfo());
            new_info.extendWithHashMap(map);
            _carrier.setSelfInfo(new_info);
            cb.invoke(null, ok);
        }catch(CarrierException e){
            util.error("[setSelfInfo] "+e.toString());
            cb.invoke(e.toString(), null);
        }
    }





    public RN_CARRIER getByName(String name){
        RN_CARRIER _rn = ALL_MAP.get(name);
        if(_rn == null){
            util.error(name+" instance not exist");
        }
        return _rn;
    }
    public Carrier getInstanceByName(String name){
        RN_CARRIER _rn = getByName(name);
        return _rn.getCarrierInstance();
    }
}
