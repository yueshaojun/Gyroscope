# Gyroscope使用
## Module间router
Module间router（以下简称Router），是用来处理Module之间的调用和监听的。具体包括两个部分，provider和receiver。
### provider
provider是module提供服务的一方，主要由xml约束，xml的格式：

<?xml version="1.0" encoding="utf-8"?>
<router>
    <!--组件应该和view对应起来-->
    <component name = "yueshaojun" path = "">
        <action name = "yueshaojun"/>
        <response path=""/>
    </component>
    <!--block 为可执行代码块，即依赖引用的代码块-->
    <block name = "block1" path = "">
        <action name = "yueshaojun"/>
    </block>
    <!--provider 为服务提供方,即依赖的module-->

    <provider name = "" path = "">
        <block name = "addPublicKey">
            <action name = "yueshaojun12"/>
        </block>
        <notification path = ""/>
    </provider>
</router>

* component，组件可以依托Activity；
* block不推荐；
* 推荐provider；

名词解释：

* provider节点包括name，path；name为provider名，自定义；path为要提供服务的module暴露的对外api类（以下简称API类）的全路径，name和path均不能为空。
* block为目标类类的public方法，由目标类提供，供外部调用。
* notification节点是API类对外提供的监听注册类。

规则说明：

* action的name应该是唯一的
* block，独立使用时必须指定path，即所在的类全路径，在provider中默认为provider的路径；name必须为block所在类的方法名
* notification 的path为provider提供的监听器全路径，provider必须提供addXXX方法，XXX即为notification。
* provider必须提供一个init（context）的方法，并且构造方法是public的，在在router中会是个单例。
* init方法会在子线程执行。

### Receiver
Receiver是对应provider的接受方，主要是接受notification的监听。由编译时注解完成。
使用方法：
在需要接受notifacation的类里使用InjectHelper.injectReceiver(this);
然后在需要接受的方法上添加注解@Receiver(methodName = "XXX")其中XXX为监听器中的回调方法名。

## Module内总线
Module内的总线由三部分组成：
1. URIString
2. 一个子线程不退出的延时队列（DelayQueue）。
3. intent 字段接收

### URIString
URIString 包括schemaType、HostType、ActionId，Field分别都用注解表示：
* schemaType：http（s），Local两种。
* HostType：View(Activity)、Component、Block
* ActionId:Host的唯一标识
* Field字段名

生成方式，运行时注解：
Router.Create(XXX.Class)；
XXX为一个interface，其中的方法由Uri、FullUri、schemaType、HostType、ActionId，Field、Delay（延时执行）修饰。
### Intent字段接收

目前Intent字段接收仅支持Activity。使用编译时注解。
使用方法：
InjectHelper.injectField(this);注册
@IntentField(name = "XXX")修饰非private变量。XXX为Field修饰的变量名。






