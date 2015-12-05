# BgHandler

## 简介
- 使用HandlerThread来封装在后台执行简单任务的处理器。

## 参考
- [详解 Android 中的 HandlerThread](http://droidyue.com/blog/2015/11/08/make-use-of-handlerthread/)

## 摘要
- 在子线程中使用Handler处理任务时必须在该线程中初始化一个Looper对象，然后使用该Looper对象初始化的Handler，
然后就可以使用该Handler在该子线程中执行异步任务
  ```java
  new Thread(){
      public void run(){
          Looper.prepare();
          Handler threadHandler = new Handler(Looper.myLooper());
          
          Looper.loop();
      }
  
  }.start();
  ```

- HandlerThread类类似上述实现过程，在初始化后直接开始线程后就可以使用该对象获得其线程中的Looper来自定义一个
Handler来处理任务，由于其是单一子线程，故适合于执行简单的非耗时阻塞式任务，如文件/数据库/SP等IO读写，**不适合
类似网络请求等耗时操作**。

- 可以使用HandlerThread去封装一些执行简单后台任务的方法，注意在使用结束后需要调用`HandlerThead.quit()`方法，否则
可能会造成Activity等的内存泄漏；同时若退出后还有消息发送或任务添加，则会产生一个`Handler sending message to a 
Handler on a dead thread`异常，封装时要注意。

## License
    Copyright 2015 Paul Chang

     Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
