# SUZU-Back-End
The back-end server for SUZU project.
## Introduction
This project mainly is about using WebSocket to build an Instant-Messaging application.
### Features
- token generated pear-to-pear communication and also for groups
- bcrypt encryption.
- authentication based on token.
- rabbitmq and redis for hot data(will do).
- maybe else coooooool tech.
### Prerequisites
- Java 15(16)
- Maven
- MySQL

---

## Pivotal knowledge
### [WebSocket]前后端同步学习

#### 为什么要使用WebSocket

这个原因也是自己一直想做一个 IM 聊天, 可以说是必经之路吧



#### 怎么实现?

##### 背景

实际上一开始是毫无头绪的, 在看了许多demo和网上的许多教程之后, 逐步了解了一点思路的过程.

即时通讯在我一开始的, 没有任何知识背景的理解下简单通俗的认为就是使用Socket通信, 后续深入的内容可以说是一点都没想到.

在看到一篇和我当前技术栈相似的[文章](https://blog.csdn.net/qq_29164699/article/details/115032418)后有了点思路.



##### 想法

还是刚开始时想的是不使用 WebSocket 而是使用 tcp/ip或udp 的方式实现, 但是甚至直接卡在了开头, 不知道怎么发, 发给谁

后来的主要思路就是 后端SpringBoot 使用 Spring 自带的 WebSocket 模块和前端进行通信, 但是问题就在于 Spring 和 React 中的 WebSocket 实在无从下手.

有说用 netty-socketio 作为后端实现的, 不过鶸根本不会 netty. 而前端则看到有使用 Socket.io 实现 Socket 的. 虽然其服务端也提供有 java [实现](https://codertw.com/%E7%A8%8B%E5%BC%8F%E8%AA%9E%E8%A8%80/690968/) . 并不是不考虑, 但是后来决定先看看 Spring 官方怎么说.

Spring 官方给出了一份 guide , 其使用的是 springboot中的 WebSocket 和 jsp 用 SockJS 和 STOMP, 最终实现的一个迎客demo:

![image-20210728191800621](pic/image-20210728191800621.png)

通过学习demo中的源码, 我也了解到了一些~~主流~~的实现方式.



###### Spring[官方文档](https://spring.io/guides/gs/messaging-stomp-websocket/)实现思路

首先看 后端实现, 使用一个配置类实现 `WebSocketMessageBrokerConfigurer` 接口中的 `configureMessageBroker` 方法 和 `registrerStompEndpoints` 方法 分别配置了基于内存的消息代理/映射 和 STOMP 相关的操作(不懂).

`enableSimpleBroker()` 指定了 url 前缀 以过滤消息代理

`setApplicationDestinationPrefixes()` 为包含 `@MessageMapping` 注解的方法定义了url前缀

`addEndpoint` 是为 `SockJS` 指定了回退选项, 也就是说如果 `WebSocket` 无法使用就在这个路径下尝试其他的传输方式.

![image-20210728192438788.png](https://i.loli.net/2021/08/13/ReLNZW9DEokdyrY.png)

然后就是一个简单的 Controller 和 两个对应的pojo (Greeting/HelloMessage):

![image-20210728192548263.png](https://i.loli.net/2021/08/13/56iL4VJQbsfmoZv.png)

`@MessageMapping(value)` 代表着要对 `value` 这个相对地址的消息请求进行处理. 那么把 在配置类中`prefixes`和`value` 连起来就代表了整体的Message请求 url

`@SendTo` 代表着最终要将这个 `Greeting` 对象发送到哪.



接下来在来看看前端的实现:

点击 Connect 这个按钮的触发事件为:

![image-20210728195903994.png](https://i.loli.net/2021/08/13/WtbX2YHQ4dCvzqV.png)

没看stomp, 大致能够猜到是订阅了 `/topic/greetings` , 当这个 url 下发布消息时, 将会调用后面的 `showGreeting()` 做出显示.

而点击 send 则会触发 `sendName()` 这个方法:

![image-20210728200426801.png](https://i.loli.net/2021/08/13/vQZqMgSN7T3exUm.png)

应该是指Sock.js会向 `/app/hello` 发送在提示框中输入的内容.

那么整个逻辑就连接起来了, 这就是一个发布订阅模式的应用, 也就是前端订阅 topic 下的内容, 并在 send 事件中发送信息到后端 Controller 中包含 `@MessageMapping(url)` 并且 url 相对应的方法中. 在后端中进行处理后再发送到 `@SendTo()` 指定 url 的位置. 这时 `stompClient` 订阅了消息, 并将其消费.





- [ ] socket.io
- [ ] https://mp.weixin.qq.com/s/rNzHvE1T_gs9AC0Fqr5K-g
- [ ] https://mp.weixin.qq.com/s/bzxX2oKRsX_h-AedjMoMqA



###### Socket.IO ? Sock.JS ? Stomp.JS !

在放弃使用 Socket.IO 之后, 一直在犹豫如何使用 SockJS 这个东西. 但是显然摆在眼前的就只有一条思路, 就是 Stomp over WebSocket.



##### 行动

> 行动上实际真的出现了很多问题... 不过解决之后也让我轻松了不少.

###### 分析问题

####### Stomp over WebSocket

> 由于仅有的参考就是spring官方给的原生js写的 `Stomp over WebSocket` . 例子及其精简, 细节很少, 所以遇到了蛮多的坑.

首先, 如果要使用 Stomp Client, 可以直接使用 `Stomp.over(url)` 进行创建.

```jsx
const sock = new SockJS('http://localhost:8080/ws');
let StompClient = Stomp.over(sock);
```

这样就算创建成功了.

要注意的是: 这里是使用SockJS进行创建的, url 一定要写全, 包括 http/ws.



####### Stomp in React

Stomp的基本初始化操作掌握了, 那么如何将其放在 React 中进行管理呢?

以 class 组件为例:

```jsx
export const sock = new SockJS('http://localhost:8080/ws');
export let StompClient = Stomp.over(sock);
export default class Socket extends React.Component {
    constructor(props) {
        super(props);
       	stompClient.connect({}, function(e) {
            console.log('连接成功', e);
            stompClient.subscribe('/topic/greeting', function(e) {
                console.log('服务端返回数据', JSON.stringify(e));
            })
        })
    }
    
    componentDidMount() {
        
    }
    
    render(
    	return (
        	<div>
         		<Form onFinish={e => {
                    console.log('success ', e);
                    stompClient.send('/app/hello', {}, JSON.stringify(e));
                }}>
                    <Form.Item label="name" name="name">
                        <Input />
                    </Form.Item>
                    <Form.Item>
                        <Button type="primary" htmlType="submit" >
                            submit
                        </Button>
                    </Form.Item>
                </Form>
         	</div>
        )
    )
}
```

这里断开连接没写. 不过也是一个意思.



####### CORS

为什么要说解决跨域问题呢? 不是说 WebSocket 没有跨域问题吗?

其实我一开始也有这样的疑惑, 但是 WebSocket 在进行握手的时候是会发送 get 请求的, 这里就会产生跨域问题.

这个其实折磨了我半天的时间, 最终总结了一点思路.(以后端解决跨域为主)



spring 自己有很多种解决跨域的方法, 但是要单单配置 WebSocket 这一项, 实际上只能在其 WebSocket 配置类中配置才能起作用.

也就是这个消息代理 `WebSocketMessageBrokerConfigurer` .

```java
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    /*
    * message broker 消息代理
    * */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
//        simpleBroker 就是一个基于内存的消息代理
//        这里的 destinationPrefixes 代表的是将消息代理携带的内容返回 /topic 下的位置
        config.enableSimpleBroker("/topic");
//        这是定义消息映射 指定了在哪个 url 下处理请求信息
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins("http://localhost:3000") //关键
                .withSockJS();
    }
}
```







