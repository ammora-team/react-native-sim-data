package eu.sigrlami.rnsimdata;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import android.os.Build;
import java.lang.reflect.Method;

public class RNSimDataModule extends ReactContextBaseJavaModule {

  ReactApplicationContext reactContext;

  TelephonyManager telManager;

  public RNSimDataModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "RNSimDataModule";
  }

  @Override
  public Map<String, Object> getConstants() {

    final Map<String, Object> constants = new HashMap<>();

    try {
      telManager = (TelephonyManager) this.reactContext.getSystemService(Context.TELEPHONY_SERVICE);

      SubscriptionManager manager = (SubscriptionManager) this.reactContext.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
      List<SubscriptionInfo> subscriptionInfos = manager.getActiveSubscriptionInfoList();

      int sub = 0;
      for (SubscriptionInfo subInfo : subscriptionInfos) {
        CharSequence carrierName = subInfo.getCarrierName();
        String countryIso        = subInfo.getCountryIso();
        int dataRoaming          = subInfo.getDataRoaming();  // 1 is enabled ; 0 is disabled
        CharSequence displayName = subInfo.getDisplayName();
        String iccId             = subInfo.getIccId();
        int mcc                  = subInfo.getMcc();
        int mnc                  = subInfo.getMnc();
        String number            = subInfo.getNumber();
        int simSlotIndex         = subInfo.getSimSlotIndex();
        int subscriptionId       = subInfo.getSubscriptionId();
        int defaultSubscriptionId = manager.getDefaultSubscriptionId();
        boolean networkRoaming   = telManager.isNetworkRoaming();
        String deviceId          = telManager.getDeviceId(simSlotIndex);
        int simState             = telManager.getSimState();
        int dataActivity         = telManager.getDataActivity();

        //String deviceId          = telManager.getImei(simSlotIndex) || telManager.getMeid(simSlotIndex);

        constants.put("carrierName" + sub, carrierName.toString());
        constants.put("displayName" + sub, displayName.toString());
        constants.put("countryCode" + sub, countryIso);
        constants.put("mcc" + sub, mcc);
        constants.put("mnc" + sub, mnc);
        constants.put("isNetworkRoaming" + sub, networkRoaming);
        constants.put("isDataRoaming"    + sub, (dataRoaming == 1));
        constants.put("simSlotIndex"     + sub, simSlotIndex);
        constants.put("phoneNumber"      + sub, number);
        constants.put("deviceId"         + sub, deviceId);
        constants.put("simSerialNumber"  + sub, iccId);
        constants.put("dataActivity"     + sub, dataActivity);
        constants.put("subscriptionId"   + sub, subscriptionId);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
          constants.put("defaultSubscriptionId" + sub, defaultSubscriptionId);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
          constants.put("simState" + sub, telManager.getSimState(simSlotIndex));
        } else {
          constants.put("simState" + sub, simState);
        }

        sub++;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return constants;
  }
}