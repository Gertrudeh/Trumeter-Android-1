
package com.iconasystems.android.trumeter.util;

public interface LifecycleProvider {

    void addLifecycleListener(LifecycleListener listener);

    void removeLifecycleListener(LifecycleListener listener);
}
