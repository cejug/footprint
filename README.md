footprint
=========

Gera PDF assinado de certificado de participação.

O template precisa conter 5 chaves ( campos no formulário pdf ):

* member
* userGroup
* eventName
* eventTime
* venue

### Usando

```java

Map<String, String> values = new HashMap<>();
values.put("member", "Fulano de tal");
values.put("userGroup", "CEJUG");
values.put("eventName", "CCTExpress");
values.put("eventTime", "01/01/2014 10:00");
values.put("venue", "Canal do CEJUG no Youtube");

FootPrint footPrint = new FootPrint(values, URL url);
footPrint.generate();

ou

footPrint = new FootPrint(values, String fileName);
footPrint.generate();

ou

footPrint = new FootPrint(values, InputStream inputStream);
footPrint.generate();

```


### 
