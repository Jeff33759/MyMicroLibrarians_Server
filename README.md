# MyMicroLibrarians_Server
分散式架構實作館藏系統


***＊＊＊＊＊＊＊＊＊＊＊＊＊專題大綱＊＊＊＊＊＊＊＊＊＊＊＊＊***

市面上虛擬主機的發展已臻成熟，隨著雲端的使用愈趨普遍，加上前後端分離的趨勢，分散式的系統架構逐漸受到歡迎。

有別於傳統的主從式架構，分散式架構除了可以幫助人們採用更加彈性的雲端部屬方案外，也有著更高的容錯性，缺點就是管理與開發要來得更加複雜。

本專題利用Spring Boot實作了分散式系統的模型，希望在學習使用Spring Cloud之前，能夠藉由實作本專題，更加了解分散式系統的運行機制。

***＊＊＊＊＊＊＊＊＊＊＊＊＊我做到了什麼＊＊＊＊＊＊＊＊＊＊＊＊＊***

1、RESTful Api，讓前後端分離的串接更加順利。

2、gateway與各微服務間的交握、溝通，將所需的物件序列化及反序列化。

3、捨棄基於Cookie的Session機制，改用JSON Web Token(JWT)來記錄使用者的狀態，方便微服務之間共享使用者的公開資訊。

4、客製化Spring Security的認證機制，實作自製的基於帳密的認證以及基於Access Token的認證邏輯。

5、實作Spring Security授權保護機制，基於角色(Role)的授權保護，讓某些API只有當該帳號具有某種角色才能夠訪問。

6、針對CORS的處理，讓Web瀏覽器能夠將跨來源的資料使用於JS，實現前後端分離。

7、針對各微服務Server的保護，讓各微服務的Server只接受來自getaway的請求。

8、gateway以亂數實現負載均衡(參考ApiGateway > jeff.apigateway.common.util.LoadBalanceUtil)。

9、Refresh Token與Access Token的實作。

10、Refresh Token Signature使用HS256對稱型加密，加密與解密都由同個伺服端負責；Access Token Signature使用RS256非對稱型加密，私鑰加密、公鑰解密，加密與解密分別由不同伺服端負責。

11、利用AOP將Logging邏輯從各業務邏輯中抽離出來，便於開發與維護。

12、將Logging分級，針對可預期的例外按照嚴重程度區分，以不同等級的Log紀錄。

13、使用RestTemplate實現各微服務間的溝通，以及與政府資料開放平台串接，下載DEMO用的館藏資料。

14、使用Mockito實作單元測試，使用MockMvc實作整合測試(只有Book Server有整合測試)。

15、使用MongoDB作為資料庫，並使用JPQL作為查詢語言，實現包含分頁查詢等等的基本CRUD功能。



![image](https://raw.githubusercontent.com/Jeff33759/MyMicroLibrarians_Server/master/System_Architecture_Diagram.jpg
)
