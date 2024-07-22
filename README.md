

Fyers Data Socket v3 Kotlin / Java JVM Desktop Android

incomplete; 


flull mode subscriptin for stocks done


lite mode for stock done


index and depth : pending




```
{
  "version": "1.0.0",
  "protocol": "1.0.0",
  "fields": [
    {
      "fid": "s100",
      "type": "Number",
      "details": "Feeder feed time [Time in seconds]",
      "index": 0,
      "fld": "ftm0",
      "dataType": "timestamp"
    },
    {
      "fid": "s101",
      "type": "Number",
      "details": "DDS feed time [Time in seconds]",
      "index": 1,
      "fld": "dtm1",
      "dataType": "timestamp"
    },
    {
      "fid": "s102",
      "type": "Number",
      "details": "Exchange feed time [Time in seconds]",
      "index": 2,
      "fld": "fdtm",
      "dataType": "timestamp"
    },
    {
      "fid": "s103",
      "type": "Number",
      "details": "Last Trade Time [Time in seconds]",
      "index": 3,
      "fld": "ltt",
      "dataType": "timestamp"
    },
    {
      "fid": "s104",
      "type": "Number",
      "details": "Trade Volume",
      "index": 4,
      "fld": "v",
      "dataType": "Number"
    },
    {
      "fid": "s105",
      "type": "Number",
      "details": "Last Trade Price",
      "index": 5,
      "fld": "ltp",
      "dataType": "ConvNumber"
    },
    {
      "fid": "s106",
      "type": "Number",
      "details": "Last Trade Quantity",
      "index": 6,
      "fld": "ltq",
      "dataType": "Number"
    },
    {
      "fid": "s107",
      "type": "Number",
      "details": "Total Buy Quantity",
      "index": 7,
      "fld": "tbq",
      "dataType": "Number"
    },
    {
      "fid": "s108",
      "type": "Number",
      "details": "Total Sell Quantity",
      "index": 8,
      "fld": "tsq",
      "dataType": "Number"
    },
    {
      "fid": "s109",
      "type": "Number",
      "details": "Best Bid Price",
      "index": 9,
      "fld": "bp",
      "dataType": "ConvNumber"
    },
    {
      "fid": "s110",
      "type": "Number",
      "details": "Best Offer Price",
      "index": 10,
      "fld": "sp",
      "dataType": "ConvNumber"
    },
    {
      "fid": "s111",
      "type": "Number",
      "details": "Best Bid Size",
      "index": 11,
      "fld": "bq",
      "dataType": "Number"
    },
    {
      "fid": "s112",
      "type": "Number",
      "details": "Best Offer Size",
      "index": 12,
      "fld": "bs",
      "dataType": "Number"
    },
    {
      "fid": "s113",
      "type": "Number",
      "details": "VWAP/Average Price",
      "index": 13,
      "fld": "ap",
      "dataType": "ConvNumber"
    },
    {
      "fid": "s114",
      "type": "Number",
      "details": "Low Price",
      "index": 14,
      "fld": "lo",
      "dataType": "ConvNumber"
    },
    {
      "fid": "s115",
      "type": "Number",
      "details": "High Price",
      "index": 15,
      "fld": "h",
      "dataType": "ConvNumber"
    },
    {
      "fid": "s116",
      "type": "Number",
      "details": "Lower Circuit Limit",
      "index": 16,
      "fld": "lcl",
      "dataType": "ConvNumber"
    },
    {
      "fid": "s117",
      "type": "Number",
      "details": "upper Circuit Limit",
      "index": 17,
      "fld": "ucl",
      "dataType": "ConvNumber"
    },
    {
      "fid": "s118",
      "type": "Number",
      "details": "Fifty Two Week High/Yearly High",
      "index": 18,
      "fld": "yh",
      "dataType": "ConvNumber"
    },
    {
      "fid": "s119",
      "type": "Number",
      "details": "Fifty Two Week Low/Yearly Low",
      "index": 19,
      "fld": "yl",
      "dataType": "ConvNumber"
    },
    {
      "fid": "s120",
      "type": "Number",
      "details": "Open Price",
      "index": 20,
      "fld": "op",
      "dataType": "ConvNumber"
    },
    {
      "fid": "s121",
      "type": "Number",
      "details": "Close Price",
      "index": 21,
      "fld": "c",
      "dataType": "ConvNumber"
    },
    {
      "fid": "s122",
      "type": "Number",
      "details": "Open Interest",
      "index": 22,
      "fld": "oi",
      "dataType": "Number"
    },
    {
      "fid": "i100",
      "type": "Number",
      "details": "Feeder feed time [Time in seconds]",
      "index": 0,
      "fld": "ftm0",
      "dataType": "timestamp"
    },
    {
      "fid": "i101",
      "type": "Number",
      "details": "DDS feed time [Time in seconds]",
      "index": 1,
      "fld": "dtm1",
      "dataType": "timestamp"
    },
    {
      "fid": "i102",
      "type": "Number",
      "details": "Last Trade Price",
      "index": 2,
      "fld": "iv",
      "dataType": "ConvNumber"
    },
    {
      "fid": "i103",
      "type": "Number",
      "details": "Close Price",
      "index": 3,
      "fld": "ic",
      "dataType": "ConvNumber"
    },
    {
      "fid": "i104",
      "type": "Number",
      "details": "Exchange feed time [Time in seconds]",
      "index": 4,
      "fld": "tvalue",
      "dataType": "timestamp"
    },
    {
      "fid": "i105",
      "type": "Number",
      "details": "High Price",
      "index": 5,
      "fld": "highPrice",
      "dataType": "ConvNumber"
    },
    {
      "fid": "i106",
      "type": "Number",
      "details": "Low Price",
      "index": 6,
      "fld": "lowPrice",
      "dataType": "ConvNumber"
    },
    {
      "fid": "i107",
      "type": "Number",
      "details": "Open Price",
      "index": 7,
      "fld": "openingPrice",
      "dataType": "ConvNumber"
    },
    {
      "fid": "d100",
      "type": "Number",
      "details": "Feeder feed time [Time in seconds]",
      "index": 0,
      "fld": "ftm0",
      "dataType": "timestamp"
    },
    {
      "fid": "d101",
      "type": "Number",
      "details": "DDS feed time [Time in seconds]",
      "index": 1,
      "fld": "dtm1",
      "dataType": "timestamp"
    },
    {
      "fid": "d102",
      "type": "Number",
      "details": "Bid Price 1 [Best Bid Price]",
      "index": 2,
      "fld": "bp",
      "dataType": "ConvNumber"
    },
    {
      "fid": "d103",
      "type": "Number",
      "details": "Bid Price 2",
      "index": 3,
      "fld": "bp1",
      "dataType": "ConvNumber"
    },
    {
      "fid": "d104",
      "type": "Number",
      "details": "Bid Price 3",
      "index": 4,
      "fld": "bp2",
      "dataType": "ConvNumber"
    },
    {
      "fid": "d105",
      "type": "Number",
      "details": "Bid Price 4",
      "index": 5,
      "fld": "bp3",
      "dataType": "ConvNumber"
    },
    {
      "fid": "d106",
      "type": "Number",
      "details": "Bid Price 5",
      "index": 6,
      "fld": "bp4",
      "dataType": "ConvNumber"
    },
    {
      "fid": "d107",
      "type": "Number",
      "details": "Offer Price 1 (Best Offer Price)",
      "index": 7,
      "fld": "sp",
      "dataType": "ConvNumber"
    },
    {
      "fid": "d108",
      "type": "Number",
      "details": "Offer Price 2",
      "index": 8,
      "fld": "sp1",
      "dataType": "ConvNumber"
    },
    {
      "fid": "d109",
      "type": "Number",
      "details": "Offer Price 3",
      "index": 9,
      "fld": "sp2",
      "dataType": "ConvNumber"
    },
    {
      "fid": "d110",
      "type": "Number",
      "details": "Offer Price 4",
      "index": 10,
      "fld": "sp3",
      "dataType": "ConvNumber"
    },
    {
      "fid": "d111",
      "type": "Number",
      "details": "Offer Price 5",
      "index": 11,
      "fld": "sp4",
      "dataType": "ConvNumber"
    },
    {
      "fid": "d112",
      "type": "Number",
      "details": "Bid Size 1 (Best Bid Size)",
      "index": 12,
      "fld": "bq",
      "dataType": "Number"
    },
    {
      "fid": "d113",
      "type": "Number",
      "details": "Bid Size 2",
      "index": 13,
      "fld": "bq1",
      "dataType": "Number"
    },
    {
      "fid": "d114",
      "type": "Number",
      "details": "Bid Size 3",
      "index": 14,
      "fld": "bq2",
      "dataType": "Number"
    },
    {
      "fid": "d115",
      "type": "Number",
      "details": "Bid Size 4",
      "index": 15,
      "fld": "bq3",
      "dataType": "Number"
    },
    {
      "fid": "d116",
      "type": "Number",
      "details": "Bid Size 5",
      "index": 16,
      "fld": "bq4",
      "dataType": "Number"
    },
    {
      "fid": "d117",
      "type": "Number",
      "details": "Offer Size 1",
      "index": 17,
      "fld": "bs",
      "dataType": "Number"
    },
    {
      "fid": "d118",
      "type": "Number",
      "details": "Offer Size 2",
      "index": 18,
      "fld": "bs1",
      "dataType": "Number"
    },
    {
      "fid": "d119",
      "type": "Number",
      "details": "Offer Size 3",
      "index": 19,
      "fld": "bs2",
      "dataType": "Number"
    },
    {
      "fid": "d120",
      "type": "Number",
      "details": "Offer Size 4",
      "index": 20,
      "fld": "bs3",
      "dataType": "Number"
    },
    {
      "fid": "d121",
      "type": "Number",
      "details": "Offer Size 5",
      "index": 21,
      "fld": "bs4",
      "dataType": "Number"
    },
    {
      "fid": "d122",
      "type": "Number",
      "details": "Bid Order 1",
      "index": 22,
      "fld": "bno1",
      "dataType": "Number"
    },
    {
      "fid": "d123",
      "type": "Number",
      "details": "Bid Order 2",
      "index": 23,
      "fld": "bno2",
      "dataType": "Number"
    },
    {
      "fid": "d124",
      "type": "Number",
      "details": "Bid Order 3",
      "index": 24,
      "fld": "bno3",
      "dataType": "Number"
    },
    {
      "fid": "d125",
      "type": "Number",
      "details": "Bid Order 4",
      "index": 25,
      "fld": "bno4",
      "dataType": "Number"
    },
    {
      "fid": "d126",
      "type": "Number",
      "details": "Bid Order 5",
      "index": 26,
      "fld": "bno5",
      "dataType": "Number"
    },
    {
      "fid": "d127",
      "type": "Number",
      "details": "Offer Order 1",
      "index": 27,
      "fld": "sno1",
      "dataType": "Number"
    },
    {
      "fid": "d128",
      "type": "Number",
      "details": "Offer Order 2",
      "index": 28,
      "fld": "sno2",
      "dataType": "Number"
    },
    {
      "fid": "d129",
      "type": "Number",
      "details": "Offer Order 3",
      "index": 29,
      "fld": "sno3",
      "dataType": "Number"
    },
    {
      "fid": "d130",
      "type": "Number",
      "details": "Offer Order 4",
      "index": 30,
      "fld": "sno4",
      "dataType": "Number"
    },
    {
      "fid": "d131",
      "type": "Number",
      "details": "Offer Order 5",
      "index": 31,
      "fld": "sno5",
      "dataType": "Number"
    },
    {
      "fid": "f101",
      "type": "Number",
      "details": "Change [Scrip]",
      "index": 25,
      "fld": "cng",
      "dataType": "ConvNumber"
    },
    {
      "fid": "f102",
      "type": "Number",
      "details": "Percentage Change [Scrip]",
      "index": 26,
      "fld": "nc",
      "dataType": "Number"
    },
    {
      "fid": "f103",
      "type": "Number",
      "details": "Turn Over",
      "index": 27,
      "fld": "to",
      "dataType": "Number"
    },
    {
      "fid": "f104",
      "type": "Number",
      "details": "Change [Index]",
      "index": 10,
      "fld": "cng",
      "dataType": "ConvNumber"
    },
    {
      "fid": "f105",
      "type": "Number",
      "details": "Percentage Change [Index]",
      "index": 11,
      "fld": "nc",
      "dataType": "Number"
    },
    {
      "fid": "f106",
      "type": "Number",
      "details": "Multiplier [Scrip]",
      "index": 23,
      "fld": "mul",
      "dataType": "Number"
    },
    {
      "fid": "f107",
      "type": "Number",
      "details": "Precision [Scrip]",
      "index": 24,
      "fld": "prec",
      "dataType": "Number"
    },
    {
      "fid": "f108",
      "type": "Number",
      "details": "Multiplier [Index]",
      "index": 8,
      "fld": "mul",
      "dataType": "Number"
    },
    {
      "fid": "f109",
      "type": "Number",
      "details": "Precision [Index]",
      "index": 9,
      "fld": "prec",
      "dataType": "Number"
    },
    {
      "fid": "f110",
      "type": "Number",
      "details": "Multiplier [Depth]",
      "index": 32,
      "fld": "mul",
      "dataType": "Number"
    },
    {
      "fid": "f111",
      "type": "Number",
      "details": "Precision [Depth]",
      "index": 33,
      "fld": "prec",
      "dataType": "Number"
    }
  ],
  "scrip": [
    "s105",
    "s104",
    "s103",
    "s102",
    "s111",
    "s112",
    "s109",
    "s110",
    "s106",
    "s107",
    "s108",
    "s113",
    "s122",
    "s114",
    "s115",
    "s118",
    "s119",
    "s116",
    "s117",
    "s120",
    "s121"
  ],
  "index": [
    "i102",
    "i103",
    "i104",
    "i105",
    "i106",
    "i107"
  ],
  "depth": [
    "d102",
    "d103",
    "d104",
    "d105",
    "d106",
    "d107",
    "d108",
    "d109",
    "d110",
    "d111",
    "d112",
    "d113",
    "d114",
    "d115",
    "d116",
    "d117",
    "d118",
    "d119",
    "d120",
    "d121",
    "d122",
    "d123",
    "d124",
    "d125",
    "d126",
    "d127",
    "d128",
    "d129",
    "d130",
    "d131"
  ],
  "user-scrip": [
    "s100",
    "s101",
    "s102",
    "s103",
    "s104",
    "s105",
    "s106",
    "s107",
    "s108",
    "s109",
    "s110",
    "s111",
    "s112",
    "s113",
    "s114",
    "s115",
    "s116",
    "s117",
    "s118",
    "s119",
    "s120",
    "s121",
    "s122",
    "f106",
    "f107",
    "f101",
    "f102",
    "f103"
  ],
  "user-index": [
    "i100",
    "i101",
    "i102",
    "i103",
    "i104",
    "i105",
    "i106",
    "i107",
    "f108",
    "f109",
    "f104",
    "f105"
  ],
  "user-depth": [
    "d100",
    "d101",
    "d102",
    "d103",
    "d104",
    "d105",
    "d106",
    "d107",
    "d108",
    "d109",
    "d110",
    "d111",
    "d112",
    "d113",
    "d114",
    "d115",
    "d116",
    "d117",
    "d118",
    "d119",
    "d120",
    "d121",
    "d122",
    "d123",
    "d124",
    "d125",
    "d126",
    "d127",
    "d128",
    "d129",
    "d130",
    "d131",
    "f110",
    "f111"
  ],
  "scrip_lite": {
    "hfid": 1
  },
  "index_lite": {
    "hfid": 1
  }
}
```

```
This Is the order of 21  feilds in full mode sf
 "scrip": [
    "s105",
    "s104",
    "s103",
    "s102",
    "s111",
    "s112",
    "s109",
    "s110",
    "s106",
    "s107",
    "s108",
    "s113",
    "s122",
    "s114",
    "s115",
    "s118",
    "s119",
    "s116",
    "s117",
    "s120",
    "s121"
  ],
```




```build.gradle```
```
plugins {
    id 'java'
    id 'org.jetbrains.kotlin.jvm'
}

group 'shakir.bhav.common'
version '1.0-SNAPSHOT'

repositories {
    mavenCentral()
    google()
}

compileKotlin {
    kotlinOptions.jvmTarget = "17"
}




dependencies {


    implementation "org.jetbrains.kotlin:kotlin-stdlib:1.8.22"
    /* implementation project(path: ':app')*/
    testImplementation group: 'junit', name: 'junit', version: '4.13.2'

    api 'org.json:json:20211205'

   // api group: 'org.jetbrains.kotlinx', name: 'kotlinx-coroutines-javafx', version: '1.4.2'


    // api 'com.google.firebase:firebase-admin:7.1.0'
    //implementation 'com.google.api-client:google-api-client:1.31.2'
    implementation "org.jetbrains.kotlin:kotlin-reflect:1.8.22"
    implementation 'com.google.protobuf:protobuf-java:4.0.0-rc-2'

    api 'com.google.code.gson:gson:2.10'


    api("io.ktor:ktor-server-core:2.0.0")
    api("io.ktor:ktor-server-netty:2.0.0")
    api("ch.qos.logback:logback-classic:1.2.10")
    api("io.ktor:ktor-client-core:2.0.3")
    api("io.ktor:ktor-client-cio:2.0.3")
    api("io.ktor:ktor-client-logging:2.0.0")
    api("io.ktor:ktor-client-gson:2.0.0")
    api("io.ktor:ktor-client-websockets:2.0.0")
    api("io.ktor:ktor-client-serialization:2.0.0")
    api("io.ktor:ktor-serialization-kotlinx-json:2.0.0")
    api("io.ktor:ktor-client-content-negotiation:2.0.0")
    api("io.ktor:ktor-serialization-gson:2.0.0")
    api("io.ktor:ktor-client-okhttp:2.0.0")
    api("io.ktor:ktor-client-apache:2.0.0")
    //api("io.ktor:ktor-client-android:2.0.0")

    api group: 'commons-codec', name: 'commons-codec', version: '1.15'
    api("com.google.guava:guava:31.0.1-android")
//    api 'io.ktor:ktor-client-android:1.6.4'



    // https://mvnrepository.com/artifact/com.squareup.okhttp/logging-interceptor





    // define a BOM and its version
    api(platform("com.squareup.okhttp3:okhttp-bom:4.10.0"))
    // define any required OkHttp artifacts without version
    api("com.squareup.okhttp3:okhttp")
    api("com.squareup.okhttp3:logging-interceptor")

    api ("org.apache.commons:commons-compress:1.22")


    api 'com.fasterxml.jackson.core:jackson-databind:2.13.4'

//    api 'javax.naming:javax.naming-api:1.0'
//    api 'javax.security:javax.security.auth.message:1.0'
//    api 'org.ietf.jgss:gssapi:1.0'


}



```


