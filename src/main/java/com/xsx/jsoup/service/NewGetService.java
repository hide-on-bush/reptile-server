package com.xsx.jsoup.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import com.xsx.jsoup.common.util.HttpUtil;
import com.xsx.jsoup.common.util.SslUtil;
import com.xsx.jsoup.entity.BalanceInfo;
import com.xsx.jsoup.entity.BankUser;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.openqa.selenium.logging.LogType;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.openqa.selenium.remote.CapabilityType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

/**
 * @Author:夏世雄
 * @Date: 2022/07/15/10:27
 * @Version: 1.0
 * @E-mail: xiashixiongtoxx@163.com
 * @Discription:
 **/
@Service
@Slf4j
public class NewGetService {

    private BankCardService bankCardService;

    public NewGetService(BankCardService bankCardService) {
        this.bankCardService = bankCardService;
    }

    public Set<JSONObject> getRequestHeaders(List<LogEntry> logs) {
        Set<JSONObject> requestHeader = new HashSet<>();
        if (!CollectionUtils.isEmpty(logs)){
            for (LogEntry entry : logs) {
                JSONObject json = JSONObject.parseObject(entry.getMessage());
                JSONObject message = json.getJSONObject("message");
                String method = message.getString("method");
                //获取请求头
                if (method != null && "Network.requestWillBeSent".equals(method)) {
                    JSONObject params = message.getJSONObject("params");
                    JSONObject request = params.getJSONObject("request");
                    String messageUrl = request.getString("url");
                    if ("https://app.my.idfcfirstbank.com/api/idp/v1/token/validate".equals(messageUrl)) {
                        requestHeader.add((JSONObject)request.get("headers"));
                    }
                }
            }
        }
        return requestHeader;
    }

    public Set<JSONObject> getRequestHeaders(LogEntries logs) {
        Set<JSONObject> requestHeader = new HashSet<>();
        for (Iterator<LogEntry> it = logs.iterator(); it.hasNext();) {
            LogEntry entry = it.next();
            JSONObject json = JSONObject.parseObject(entry.getMessage());
            JSONObject message = json.getJSONObject("message");
            String method = message.getString("method");
            //获取响应头
//            if (method != null && "Network.responseReceived".equals(method)) {
//                JSONObject params = message.getJSONObject("params");
//                JSONObject response = params.getJSONObject("response");
//                String messageUrl = response.getString("url");
//                if ("https://app.my.idfcfirstbank.com/api/tracing/span".equals(messageUrl)) {
//                    System.out.println("responseHeaders = " + response.get("headers"));
//                }
//            }
            //获取请求头
            if (method != null && "Network.requestWillBeSent".equals(method)) {
                JSONObject params = message.getJSONObject("params");
                JSONObject request = params.getJSONObject("request");
                String messageUrl = request.getString("url");
                if ("https://app.my.idfcfirstbank.com/api/idp/v1/token/validate".equals(messageUrl)) {
                    requestHeader.add((JSONObject)request.get("headers"));
                }
            }
        }
        return requestHeader;
    }

    public Map<String, Map<String, String>>  login(){
        int reties = 3;
        int runTimes = 1;
        System.setProperty("webdriver.chrome.driver", "D:/xsx-tools/chormeDriver/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        //无界面参数
        //options.addArguments("headless");
        //禁用沙盒
        options.addArguments("no-sandbox");
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setBrowserName("chrome");
        cap.setCapability(ChromeOptions.CAPABILITY, options);
        // set performance logger
        // this sends Network.enable to chromedriver
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
        cap.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
        ChromeDriver chromeDriver = new ChromeDriver(cap);
        chromeDriver.navigate().to("https://my.idfcfirstbank.com/login");
        while(reties >= 0) {
            try{
                log.info("执行次数 ={}", runTimes);
                return doLogin(chromeDriver);
            } catch (Exception e) {
                log.error("执行异常， error= {}", e);
                reties--;
                runTimes++;
            }
        }

        return null;
    }

    private Map<String,  Map<String, String>>  doLogin(ChromeDriver chromeDriver) {
        Map<String,  Map<String, String>> map = new HashMap<>();
        new WebDriverWait(chromeDriver, Duration.ofSeconds(50L)).until(ExpectedConditions
                .presenceOfElementLocated(By.id("app")));
        bankCardService.inputMobileAndSubmit(chromeDriver);
        //显示等待元素加载完毕，如果在设置的时间内还没有出现，则抛异常
        bankCardService.inputPwdAndSubmit(chromeDriver);

        List<LogEntry> logs = chromeDriver.manage().logs().get(LogType.PERFORMANCE).getAll();
        Set<JSONObject> requestHeader = getRequestHeaders(logs);
        Set<Cookie> cookies = chromeDriver.manage().getCookies();
        cookies = cookies.stream().filter(x ->"my.idfcfirstbank.com".equals(x.getDomain())).collect(Collectors.toSet());
        System.out.println("cookies = " + cookies);
        map.put(COOKIES, parseMap(JSON.toJSONString(cookies)));
        map.put(REQUEST_HEADERS, parseMap(JSON.toJSONString(requestHeader)));
        return map;
    }

    private static final String COOKIES = "cookies";
    private static final String REQUEST_HEADERS = "requestHeaders";

    private void getJson(Map<String, String>  map) {
        Object cookie = parseJson(map, COOKIES);
        Object requestHeaders = parseJson(map, REQUEST_HEADERS);
        System.out.println(cookie);
        System.out.println(requestHeaders);
        System.out.println("Authorization =" + getByKey(requestHeaders, "Authorization"));
        System.out.println("token =" + getByKey(requestHeaders, "enc-id-token"));
    }

    public String getByKey(Object obj, String key) {
        String result = null;
        if (ObjectUtils.isNotEmpty(obj)) {
            if (obj instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject) obj;
                if (jsonObject.containsKey(key)) {
                    result = jsonObject.get(key).toString();
                }
            } else {
                JSONArray array = (JSONArray) obj;
                Iterator iterator = array.iterator();
                while (iterator.hasNext()) {
                    JSONObject jsonObject = (JSONObject)iterator.next();
                    if (jsonObject.containsKey(key)) {
                        result = jsonObject.get(key).toString();
                    }
                }
            }

        }
        return result;
    }

       public Map<String,String> parseMap(String source) {
        Map<String, String> result = new HashMap<>();
        if (StringUtils.isNotBlank(source)) {
            if (source.trim().startsWith("[")) {
                JSONArray array = JSONArray.parseArray(source);
                List<JSONObject> jsonObjectList = array.toJavaList(JSONObject.class);
                if (!CollectionUtils.isEmpty(jsonObjectList)) {
                    for (JSONObject item : jsonObjectList) {
                        result.putAll((Map)JSON.parse(item.toString()));
                    }
                }
            }else {
                result = (Map) JSON.parse(source);
            }
        }
        return result;
    }

    private Object parseJson(Map<String, String> map, String key) {
        Object parseResult = null;
        if (!ObjectUtils.isEmpty(map) && map.containsKey(key)) {
            String json = map.get(key);
            if (StringUtils.isNotBlank(json)) {
                if (json.trim().startsWith("[")) {
                    parseResult = JSONArray.parseArray(json);
                } else {
                    parseResult = JSONObject.parseObject(json);

                }
            }
        }
        return parseResult;
    }


    public BankUser initParam(){
        BankUser bankUser = new BankUser();
        bankUser.setUserId("10077465974");
        bankUser.setLoginName("8197132475");
        bankUser.setPassword("Raju@0901");
        bankUser.setBankName("IDFC FIRST Bank");
        return bankUser;
    }


    public BalanceInfo getBalance()  throws Exception{
        SslUtil.ignoreSsl();
        BalanceInfo balanceInfo = null;
        Map<String, Map<String, String>> cookeisAndHeaders = this.login();
        if (!CollectionUtils.isEmpty(cookeisAndHeaders) && cookeisAndHeaders.containsKey("requestHeaders")) {
            Map<String, String> headers = cookeisAndHeaders.get("requestHeaders");
            BankUser bankUser = initParam();
            balanceInfo = this.fetchBalance(bankUser, headers);
        }
        return balanceInfo;
    }

    @Autowired
    @Qualifier("myRedisTemplate")
    private RedisTemplate redisTemplate;

    public BalanceInfo fetchBalance(BankUser bankUser, Map<String, String> headers) throws IOException {
        //this.sentByRest(headers);
        try {
           // this.doGetByHttpClient(headers);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String param= com.alibaba.fastjson.JSON.toJSONString(headers);
        //HttpResponse sender = this.sender(headers);
        //Response response = this.send(headers,"https://app.my.idfcfirstbank.com/api/payments/v1/accounts");
        //okhttpclient
        Response response = HttpUtil.sent("GET","https://app.my.idfcfirstbank.com/api/payments/v1/accounts",
                "application/json",null,headers);
        BalanceInfo balanceInfo=new BalanceInfo();
        String jsonSource= response.body().string();

        com.alibaba.fastjson.JSONObject jsonObject= com.alibaba.fastjson.JSONObject.parseObject(jsonSource);
        com.alibaba.fastjson.JSONArray jsonArray=jsonObject.getJSONArray("accountsList");
        for (int i=0;i<jsonArray.size();i++){
            com.alibaba.fastjson.JSONObject accountJson=jsonArray.getJSONObject(i);
            String accountNumber = accountJson.getString("accountNumber");
            if(bankUser.getUserId().equals(accountNumber)){
                String currentBalance=accountJson.getString("currentBalance");
                balanceInfo.setLoginName(bankUser.getLoginName());
                balanceInfo.setFetchTime(new Date());
                balanceInfo.setBankName(bankUser.getBankName());
                balanceInfo.setBalance(new BigDecimal(currentBalance));
            }
        }
        Executors.newFixedThreadPool(1).execute(()-> {
            redisTemplate.opsForValue().setIfAbsent("idfc:"+bankUser.getBankName(),com.alibaba.fastjson.JSON.toJSONString(headers),30L, TimeUnit.MINUTES);
        });
        return balanceInfo;
    }


    public Response send(Map<String, String> headers, String url) throws IOException{
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(50, TimeUnit.SECONDS)
                .readTimeout(50, TimeUnit.SECONDS)
                .build();

        //"https://app.my.idfcfirstbank.com/api/payments/v1/accounts"
        Request.Builder builder = new Request.Builder().url(url);
        if (!CollectionUtils.isEmpty(headers)) {
            headers.forEach((k,v) ->{
                log.info("k={},v={}", k, v);
                builder.addHeader(k,v);
            });
        }
        builder.removeHeader("Authorization");
        builder.addHeader("Authorization",headers.get("Authorization"));
        Request request = builder.build();


        log.info("request ={}", request.headers());
        return client.newCall(request).execute();
    }


    public HttpResponse sender(Map<String, String> headers){
        try {
            this.doGetByHttpClient(headers);
        } catch (Exception e) {
            e.printStackTrace();
        }
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("https://app.my.idfcfirstbank.com/api/account/v1?status=ACTIVE,INACTIVE,DORMANT,PRECREATED");

        if (!CollectionUtils.isEmpty(headers)) {
            headers.forEach((k,v)->{
                httpGet.setHeader(k,v);
            });
        }
        Header[] allHeaders = httpGet.getAllHeaders();

        CloseableHttpResponse response = null;
        try {
            response = client.execute(httpGet);
        } catch (IOException e) {
            log.error("httpclient调用异常：error=", e);
        }
        return  response;
    }

    public void doGetByHttpClient(Map<String, String> header) throws Exception{
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("https://app.my.idfcfirstbank.com/api/account/v1?status=ACTIVE,INACTIVE,DORMANT,PRECREATED");
        header.forEach((k,v)->{
            httpGet.setHeader(k, v);
        });
        CloseableHttpResponse response = null;
        try {
            // 执行请求，相当于敲完地址后按下回车。获取响应
            response = httpclient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                // 解析响应，获取数据
                System.out.println(response.getEntity());
            }
        } finally {
            if (response != null) {
                // 关闭资源
                response.close();
            }
            // 关闭浏览器
            httpclient.close();
        }

    }

    @Autowired
    @Qualifier("restTemplate")
    private RestTemplate restTemplate;
    public void sentByRest(Map<String, String> header){

        HttpHeaders headers = new HttpHeaders();
        HttpMethod method = HttpMethod.GET;
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
        header.forEach((k,v)->{
            headers.add(k, v);
        });

        String url = "https://app.my.idfcfirstbank.com/api/payments/v1/accounts";
        //String url = "https://app.my.idfcfirstbank.com/api/account/v1?status=ACTIVE,INACTIVE,DORMANT,PRECREATED";
        ResponseEntity<String> resEntity = restTemplate.exchange(url, method, requestEntity, String.class);


        //ResponseEntity<JSONObject> forEntity = template.getForEntity(url, JSONObject.class);

        System.out.println("forEntity=" + resEntity);
    }
}

