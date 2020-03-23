# java-tool
useful java tool !!!

## xmind-generator
能够把excel文件转换成xmind文件
限制：excel数据必须有层级关系

```
用法

java -jar ./xmind-generator-1.0-SNAPSHOT-jar-with-dependencies.jar ./excel.xlsx
java -jar ./xmind-generator-1.0-SNAPSHOT-jar-with-dependencies.jar ./excel.xlsx ./output.xmind 
java -jar ./xmind-generator-1.0-SNAPSHOT-jar-with-dependencies.jar ./excel.xlsx ./output.xmind BANK
```

数据示例:
![通用数据](xmind-generator/src/main/resources/通用数据.jpg)
![通用结果](xmind-generator/src/main/resources/通用数据结果.jpg)

## image-generator
图片爬取