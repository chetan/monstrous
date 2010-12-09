
COMMONS_CODEC = [ "commons-codec:commons-codec:jar:1.4" ]
COMMONS_COLLECTIONS = [ "commons-collections:commons-collections:jar:3.2" ]
COMMONS_FILEUPLOAD = [ "commons-fileupload:commons-fileupload:jar:1.2.1" ]
COMMONS_IO = [ "commons-io:commons-io:jar:1.3.2" ]
COMMONS_LANG = [ "commons-lang:commons-lang:jar:2.4" ]
COMMONS_LOGGING = [ "commons-logging:commons-logging:jar:1.1.1" ]
COMMONS_POOL = [ "commons-pool:commons-pool:jar:1.5.4" ]
COMMONS_NET = [ "commons-net:commons-net:jar:2.0" ]
COMMONS_DISCOVERY = [ "commons-discovery:commons-discovery:jar:0.4" ]

COMMONS_DBCP = [
    "commons-dbcp:commons-dbcp:jar:1.4"
] + COMMONS_POOL

HTTPCLIENT = [
    "org.apache.httpcomponents:httpclient:jar:4.0.3",
    "org.apache.httpcomponents:httpcore:jar:4.1",
    "org.apache.httpcomponents:httpcore-nio:jar:4.1"
    ] + COMMONS_CODEC + COMMONS_LOGGING
    
JSON_LIB = [
    "commons-beanutils:commons-beanutils:jar:1.7.0",
    "net.sf.ezmorph:ezmorph:jar:1.0.6",
    "net.sf.json-lib:json-lib:jar:2.2.3"
    ] + COMMONS_LOGGING + COMMONS_LANG + COMMONS_COLLECTIONS
    
JETTY = [
    "org.mortbay.jetty:jetty-util:jar:6.1.26",
    "org.mortbay.jetty:jetty:jar:6.1.26",
    "org.mortbay.jetty:servlet-api:jar:2.5-20081211"
    ]