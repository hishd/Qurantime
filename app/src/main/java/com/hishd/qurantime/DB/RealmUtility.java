package com.hishd.qurantime.DB;

import com.hishd.qurantime.Util.Constraints;
import io.realm.RealmConfiguration;

public class RealmUtility {
//    private static final int SCHEMA_V_PREV = 1;// previous schema version

    public static int getSchemaVNow() {
        return Constraints.REALM_SCHEMA_V_NOW;
    }

    public static RealmConfiguration getDefaultConfig() {
        return new RealmConfiguration.Builder()
                .schemaVersion(Constraints.REALM_SCHEMA_V_NOW)
//                .deleteRealmIfMigrationNeeded()// if migration needed then this method will remove the existing database and will create new database
                .build();
    }
}
