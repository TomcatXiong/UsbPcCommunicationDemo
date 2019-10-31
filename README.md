# UsbPcCommunicationDemo
方案实现思路：
1.设备端开启一个socket服务，服务端口12345；
2.设备端监听USB插拔事件来启动关闭设备端的socket服务；
3.PC端使用adb命令：adb forward tcp:54321 tcp:12345，将PC端口54321上发来的数据转换到设备socket服务端口；
4.PC端启动socket客户端与PC端口54321进行数据通讯。

Demo实现了：
1.安卓设备端socket服务，服务监听端口12345，收到任何消息，均回复设备的毫秒时间戳。
2.PC 实现使用adb命令将PC端口54321消息转发到设备服务端口12345，之后，连接socket连接，并监听端口54321的消息，每隔3s发送PC毫秒时间戳，并监听设备端服务回应的数据。
设备端：
SocketServer:服务端接收socket信息，回复设备端的时间戳信息
PC端：
SockectClient:PC端每隔3s给监听端口发送PC时间戳信息
