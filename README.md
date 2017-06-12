&ensp;&ensp; SimonEventBus类似于Eventbus, 它简化了Activity、Fragment、Service等组件或者对象之间的交互，很大程度上降低了它们之间的耦合，使得我们的代码更加简洁，耦合性更低，提升我们的代码质量。

> 申明：本项目是学习自@MrSimp1e Android事件开发总线，所以基本上所有的技术点都是从该开源项目中学习来的，主要是用于作者本人的学习和工作使用。 

**大家可以直接看原项目，这个项目只用于作者本人的学习使用。**
http://blog.csdn.net/bboyfeiyu/article/details/44309093

---

**项目涉及到的技术点**：
- 1.java 注解
- 2.java反射
- 3.java 并发类CopyOnWriteArrayList,ConcurrentHashMap
- 4.java集合的特性Arraylist，LinkedList等
- 5.java 对象比较
- 6.android handlerThread

---

**项目的总体设计：**
![](https://github.com/curtis2/SimonEventbus/blob/master/images/QQ%E6%88%AA%E5%9B%BE20161222143439.png)

&ensp;&ensp;&ensp;AndroidEventBus简约整体设计大致如上图所示，主要分为三个部分，即EventBus、SubscriberMethodHunter、EventHandler。

&ensp;&ensp;&ensp;EventBus负责订阅对象与事件的管理，比如注册、注销以及发布事件等。在初始时将某个对象注册到EventBus中，EventBus会遍历该对象class中的所有方法，把参数数量为1且用了Subscriber注解标识的函数管理起来，以事件类型和订阅函数Subscriber的tag构建一个EventType作为一种事件类型，某个事件类型对应有一个接收者列表。当有事件发布时，EventBus会根据发布的事件类型与tag构建EventType，然后找到对应的订阅者列表，并且将这些事件投递给所有订阅者。SubscriberMethodHunter负责查找合适的EventType，而EventHandler则负责将这些订阅函数执行到相应的线程中。至此，整个事件总线的操作流程就完成了，当然在将Activity、Fragment等组件注册到EventBus时，不要忘了在这些对象销毁时将它们从EventBus中移除，即调用unregister方法。


**事件注册流程图**
![](https://github.com/curtis2/SimonEventbus/blob/master/images/QQ%E6%88%AA%E5%9B%BE20161222143450.png)
**事件发布流程图**
![](https://github.com/curtis2/SimonEventbus/blob/master/images/QQ%E6%88%AA%E5%9B%BE20161222143500.png)

 **核心类详细介绍**
- EventBus : 事件总线核心类，封装了订阅对象的注册、注销以及事件的发布、投递等，是事件总线最核心的类;
- EventType : 事件类型对象，由事件Class类型与接收函数tag组成，用于标识一个事件类型;
- Subscriber : Subscriber注解，作用于函数上，用于标识这个函数是一个事件订阅函数;
- ThreadMode : 接收函数的执行线程模型,默认执行在UI线程;
- Subscription : 一个订阅对象的封装类，含有接收对象、目标方法、线程模型;
- TargetMethod : 目标订阅方法;
- SubsciberMethodHunter : 订阅方法查找辅助类，根据事件对象查找符合要求的EventType列表，EventBus根据这个返回的列表来投递事件给订阅者;
- EventHandler : 事件处理器;
- DefaultEventHandler : 默认的事件处理器,即事件在哪个线程投递，则处理事件也在该线程;
- UIThreadEventHandler : 事件处理在UI线程的Handler;
- AsyncEventHandler : 事件处理在一个独立线程的Handler;
- MatchPolicy : 事件匹配策略接口;
- DefaultMatchPolicy : 默认的匹配策略，发布事件时，
- EventBus会查找参数是该事件父类的函数，并且构造对应的EventType;
- StrictMatchPolicy : 严格的匹配策略，发布事件时只查找参数类型完全匹配的订阅函数;
