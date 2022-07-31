# MyMicroLibrarians_Server
分散式架構實作館藏系統

***＊＊＊＊＊＊＊＊＊＊＊＊＊專題大綱＊＊＊＊＊＊＊＊＊＊＊＊＊***

隨著市面上虛擬主機的發展及使用愈發成熟，越來越多人租用虛擬主機來運營系統，加上前後端分離的趨勢，分散式的系統架構逐漸受到歡迎。

有別於傳統的主從式架構，分散式架構除了可以幫助人們採用更加彈性的部屬方案外，也有著更高的容錯性，缺點就是管理與開發要來得更加困難。

本專題利用Spring Boot實作了分散式系統的模型，希望在學習使用Spring Cloud之前，能夠藉由實作本專題，更加了解分散式系統的運行機制。

***＊＊＊＊＊＊＊＊＊＊＊＊＊我做到了什麼＊＊＊＊＊＊＊＊＊＊＊＊＊***

1、RESTful Api，幫助前後端分離的串接。
2、捨棄基於Cookie的Session機制，改用JSON Web Token(JWT)來記錄使用者的狀態，方便不同服務端間共享使用者的資訊。
3、利用AOP插入Logging邏輯來記錄

![image](https://raw.githubusercontent.com/Jeff33759/MyMicroLibrarians_Server/master/System_Architecture_Diagram.jpg
)
