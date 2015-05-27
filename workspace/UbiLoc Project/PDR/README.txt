定位算法注意事项：
开启服务、获得定位结果、关闭服务在com.ubirtls.view.Activity.MapActivity里面实现。注意PDRService服务要在AndroidManifest.xml里面注册。

com.ubirtls.PDR.PDRService：PDR方法关键服务，实时采集样本数据，并计算位置偏移。其中ParticleFilter particleFilter = new ParticleFilter(78, 4)指定了定位目标的初始位置。

com.ubirtls.PDR.HeadingKalmanFilter：陀螺仪、电子罗盘数据通过卡尔曼滤波获得较准确的步行朝向。其中private double pre_ori = 90指定了定位目标的初始朝向。

com.ubirtls.PDR.StepDetector：捕获步行事件,并计算每步的步长。其中private double K = 0.56指定了步长参数。

com.ubirtls.PDR.CornerDetector：捕获转角。其中private static final String cornersInfoFile = "client/corners.txt"指定环境中的一些转角坐标，用于矫正。corners.txt对程序没什么影响。

com.ubirtls.particlefilter.WallDetector：检测粒子在传播过程中是否“撞墙”,检测目标是否被定位到墙壁内。其中 private static final String wallsInfoFile = "client/walls.txt"确定周边墙壁。墙壁文件walls.txt注意导入项目程序。walls.txt：粒子滤波+Map来约束，用于精准估计位置。

com.ubirtls.util.MyMath 实现PDR和粒子滤波算法需要用到的数学公式。