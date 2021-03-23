package com.huawei.myphonegame2.slice;

import com.huawei.myphonegame2.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.*;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.utils.Color;
import ohos.agp.utils.RectFloat;
import ohos.agp.utils.TextAlignment;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

import java.util.Timer;
import java.util.TimerTask;

public class MainAbilitySlice extends AbilitySlice {
    private DirectionalLayout layout;//布局
    private static final int length=100;//方格的边长
    private static final int interval=2;//方格的间距
    private static final int[][] RedGrids1={{0,3},{0,4},{1,4},{1,5}};//红色方块形态1
    private static final int[][] RedGrids2={{0,5},{1,5},{1,4},{2,4}};//红色方块形态2
    private static final int[][] GreenGrids1={{0,5},{0,4},{1,4},{1,3}};//绿色方块形态1
    private static final int[][] GreenGrids2={{0,4},{1,4},{1,5},{2,5}};//绿色方块形态2
    private static final int[][] CyanGrids1={{0,4},{1,4},{2,4},{3,4}};//蓝绿色方块形态1
    private static final int[][] CyanGrids2={{0,3},{0,4},{0,5},{0,6}};//蓝绿色方块形态2
    private static final int[][] MagentaGrids1={{0,4},{1,3},{1,4},{1,5}};//品红色方块形态1
    private static final int[][] MagentaGrids2={{0,4},{1,4},{1,5},{2,4}};//品红色方块形态2
    private static final int[][] MagentaGrids3={{0,3},{0,4},{0,5},{1,4}};//品红色方块形态3
    private static final int[][] MagentaGrids4={{0,5},{1,5},{1,4},{2,5}};//品红色方块形态4
    private static final int[][] BlueGrids1={{0,3},{1,3},{1,4},{1,5}};//蓝色方块形态1
    private static final int[][] BlueGrids2={{0,5},{0,4},{1,4},{2,4}};//蓝色方块形态2
    private static final int[][] BlueGrids3={{0,3},{0,4},{0,5},{1,5}};//蓝色方块形态3
    private static final int[][] BlueGrids4={{0,5},{1,5},{2,5},{2,4}};//蓝色方块形态4
    private static final int[][] WhiteGrids1={{0,5},{1,5},{1,4},{1,3}};//白色方块形态1
    private static final int[][] WhiteGrids2={{0,4},{1,4},{2,4},{2,5}};//白色方块形态2
    private static final int[][] WhiteGrids3={{0,5},{0,4},{0,3},{1,3}};//白色方块形态3
    private static final int[][] WhiteGrids4={{0,4},{0,5},{1,5},{2,5}};//白色方块形态4
    private static final int[][] YellowGrids={{0,4},{0,5},{1,5},{1,4}};//黄色方块形态1
    private static final int grids_number=4;//方块所占方格的数量，固定为4
    private int[][] grids;//描绘方格颜色的二维数组
    private int[][] NowGrids;//当前方块的形态
    private int row_number;//方块的总行数
    private int column_number;//方块的总列数
    private int Nowrow;//向下移动的行数
    private int Nowcolumn;//向左右移动的列数，减1表示左移，加1表示右移
    private int Grids;//当前方格的颜色，0表示灰色，1代表红色，2代表绿色，3代表蓝绿色，4代表品红色，5代表蓝色，6代表白色，7代表黄色
    private int column_start;//方块的第一个方格所在二维数组的列数
    private Timer timer;//时间

    public void onStart(Intent intent) {
        super.onStart(intent);

        initialize();
        run();
    }

    public void initialize(){//(重新)开始游戏
        layout = new DirectionalLayout(this);
        grids = new int[15][10];
        for(int row = 0; row < 15; row++)
            for(int column = 0; column < 10; column++)
                grids[row][column] = 0;

        createGrids();
        drawButton();
        drawGrids();
    }

    public void createGrids(){//随机生成一个颜色方块
        Nowcolumn = 0;
        Nowrow = 0;

        eliminateGrids();

        double random = Math.random();
        if(random >= 0 && random < 0.2){
            if(random >= 0 && random < 0.1)
                createRedGrids1();
            else
                createRedGrids2();
        }
        else if(random >= 0.2 && random < 0.4){
            if(random >= 0.2 && random < 0.3)
                createGreenGrids1();
            else
                createGreenGrids2();
        }
        else if(random >= 0.4 && random < 0.45){
            if(random >= 0.4 &&random < 0.43)
                createCyanGrids1();
            else
                createCyanGrids2();
        }
        else if(random >= 0.45 && random < 0.6){
            if(random >= 0.45 && random < 0.48)
                createMagentaGrids1();
            else if(random >= 0.48 && random < 0.52)
                createMagentaGrids2();
            else if(random >= 0.52 && random < 0.56)
                createMagentaGrids3();
            else
                createMagentaGrids4();
        }
        else if(random >= 0.6 && random < 0.75){
            if(random >= 0.6 && random < 0.63)
                createBlueGrids1();
            else if(random >= 0.63 && random < 0.67)
                createBlueGrids2();
            else if(random >= 0.67 && random < 0.71)
                createBlueGrids3();
            else
                createBlueGrids4();
        }
        else if(random >= 0.75 && random < 0.9){
            if(random >= 0.75 && random < 0.78)
                createWhiteGrids1();
            else if(random >=0.78 && random < 0.82)
                createWhiteGrids2();
            else if(random >=0.82 && random < 0.86)
                createWhiteGrids3();
            else
                createWhiteGrids4();
        }
        else {
            createYellowGrids();
        }

        if(gameover() == false){//当游戏未有结束时才生成新的颜色方块
            for(int row = 0; row < grids_number; row++){//将颜色方块对应的Grids添加到二维数组中
                grids[NowGrids[row][0] + Nowrow][NowGrids[row][1] + Nowcolumn] = Grids;
            }
        }
        else{//当游戏结束时停止时间和绘制游戏结束文本
            timer.cancel();
            drawText();
        }
    }

    public void drawButton(){//绘制按钮
        ShapeElement background = new ShapeElement();
        background.setRgbColor(new RgbColor(174, 158, 143));
        background.setCornerRadius(100);

        Button button1 = new Button(this);
        button1.setText("←");
        button1.setTextAlignment(TextAlignment.CENTER);
        button1.setTextColor(Color.WHITE);
        button1.setTextSize(100);
        button1.setMarginTop(1800);
        button1.setMarginLeft(160);
        button1.setPadding(10,0,10,0);
        button1.setBackground(background);
        button1.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                leftShift();
            }
        });
        layout.addComponent(button1);

        Button button2 = new Button(this);
        button2.setText("变");
        button2.setTextAlignment(TextAlignment.CENTER);
        button2.setTextColor(Color.WHITE);
        button2.setTextSize(100);
        button2.setMarginLeft(480);
        button2.setMarginTop(-130);
        button2.setPadding(10,0,10,0);
        button2.setBackground(background);
        button2.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                changGrids();
            }
        });
        layout.addComponent(button2);

        Button button3 = new Button(this);
        button3.setText("→");
        button3.setTextAlignment(TextAlignment.CENTER);
        button3.setTextColor(Color.WHITE);
        button3.setTextSize(100);
        button3.setMarginLeft(780);
        button3.setMarginTop(-130);
        button3.setPadding(10,0,10,0);
        button3.setBackground(background);
        button3.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                rightShift();
            }
        });
        layout.addComponent(button3);

        Button button = new Button(this);
        button.setText("重新开始");
        button.setTextSize(100);
        button.setTextAlignment(TextAlignment.CENTER);
        button.setTextColor(Color.WHITE);
        button.setMarginTop(5);
        button.setMarginLeft(310);
        button.setPadding(10,10,10,10);
        button.setBackground(background);
        button.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                initialize();
                run();
            }
        });
        layout.addComponent(button);
    }

    public void drawGrids(){//绘制背景图和方格
        layout.setLayoutConfig((new ComponentContainer.LayoutConfig(ComponentContainer.LayoutConfig.MATCH_PARENT,ComponentContainer.LayoutConfig.MATCH_PARENT)));

        Component.DrawTask task=new Component.DrawTask() {
            @Override
            public void onDraw(Component component, Canvas canvas) {
                Paint paint = new Paint();

                paint.setColor(Color.BLACK);
                RectFloat rect=new RectFloat(30-20,250-20,length*10+interval*9+30+20,length*15+interval*14+250+20);
                canvas.drawRect(rect,paint);//绘制背景图

                for(int row = 0; row < 15; row++){//0表示灰色，1代表红色，2代表绿色，3代表蓝绿色，4代表品红色，5代表蓝色，6代表白色，7代表黄色
                    for(int column = 0; column < 10; column++){
                        if(grids[row][column] == 0)
                            paint.setColor(Color.GRAY);
                        else if(grids[row][column] == 1)
                            paint.setColor(Color.RED);
                        else if(grids[row][column] == 2)
                            paint.setColor(Color.GREEN);
                        else if(grids[row][column] == 3)
                            paint.setColor(Color.CYAN);
                        else if(grids[row][column] == 4)
                            paint.setColor(Color.MAGENTA);
                        else if(grids[row][column] == 5)
                            paint.setColor(Color.BLUE);
                        else if(grids[row][column] == 6)
                            paint.setColor(Color.WHITE);
                        else if(grids[row][column] == 7)
                            paint.setColor(Color.YELLOW);
                        RectFloat rectFloat=new RectFloat(30+column*(length+interval),250+row*(length+interval),30+length+column*(length+interval),250+length+row*(length+interval));
                        canvas.drawRect(rectFloat,paint);//绘制方格
                    }
                }
            }
        };

        layout.addDrawTask(task);
        setUIContent(layout);
    }

    public void changGrids(){//变换方块的形状
        for(int row = 0; row < grids_number; row++){//将原来的颜色方块清除
            grids[NowGrids[row][0] + Nowrow][NowGrids[row][1] + Nowcolumn] = 0;
        }
        if(column_number == 2 && Nowcolumn + column_start == 0){
            Nowcolumn++;
        }

        //根据Grids的颜色值调用改变方块形状的"chang+Color+Grids"函数
        if(Grids==1){
            changRedGrids();
        }
        else if(Grids==2){
            changeGreenGrids();
        }
        else if(Grids==3){
            changeCyanGrids();
        }
        else if(Grids==4){
            changeMagentaGrids();
        }
        else if(Grids==5){
            changeBlueGrids();
        }
        else if(Grids==6){
            changeWhiteGrids();
        }

        for(int row = 0; row < grids_number; row++){//重新绘制颜色方块
            grids[NowGrids[row][0] + Nowrow][NowGrids[row][1] + Nowcolumn] = Grids;
        }

        drawGrids();
    }

    public void drawText(){//绘制游戏结束文本
        Text text=new Text(this);
        text.setText("游戏结束");
        text.setTextSize(100);
        text.setTextColor(Color.BLUE);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setMarginsTopAndBottom(-2000,0);
        text.setMarginsLeftAndRight(350,0);
        layout.addComponent(text);
        setUIContent(layout);
    }

    public void run(){//方块随着时间逐渐下移
        timer=new Timer();
        timer.schedule(new TimerTask() {//每隔750毫秒下移一格
            @Override
            public void run() {
                getUITaskDispatcher().asyncDispatch(()->{
                    if(down()){//如果方块能下移则下移，否则重新随机生成新的方块
                        for(int row = 0; row < grids_number; row++){//将原来的颜色方块清除
                            grids[NowGrids[row][0] + Nowrow][NowGrids[row][1] + Nowcolumn] = 0;
                        }
                        Nowrow++;
                        for(int row = 0; row < grids_number; row++){//重新绘制颜色方块
                            grids[NowGrids[row][0] + Nowrow][NowGrids[row][1] + Nowcolumn] = Grids;
                        }
                    }
                    else{
                        createGrids();
                    }
                    drawGrids();
                });
            }
        },0,750);
    }

    public boolean down(){//判断方块能否下移
        boolean k;
        if(Nowrow + row_number == 15){//如果方块向下移动到下边界，则返回false
            return false;
        }

        for(int row = 0; row < grids_number; row++){//当下边缘方块再下一格为空时则可以下移
            k = true;
            for(int i = 0; i < grids_number; i++){
                if(NowGrids[row][0] + 1 == NowGrids[i][0] && NowGrids[row][1] == NowGrids[i][1]){//找出非下边缘方块
                    k = false;
                }
            }
            if(k){//当任一下边缘方块再下一格不为空时返回false
                if(grids[NowGrids[row][0] + Nowrow + 1][NowGrids[row][1] + Nowcolumn] != 0)
                    return false;
            }
        }

        return true;
    }

    public void leftShift(){//方块向左移动一格
        if(left()){//当方块能向左移动时则左移
            for(int row = 0; row < grids_number; row++){//将原来的颜色方块清除
                grids[NowGrids[row][0] + Nowrow][NowGrids[row][1] + Nowcolumn] = 0;
            }
            Nowcolumn--;
            for(int row = 0; row < grids_number; row++){//重新绘制颜色方块
                grids[NowGrids[row][0] + Nowrow][NowGrids[row][1] + Nowcolumn] = Grids;
            }
        }
        drawGrids();
    }

    public boolean left(){//判断方块能否向左移动
        boolean k;
        if(Nowcolumn + column_start == 0){//如果方块向左移动到左边界，则返回false
            return false;
        }

        for(int column = 0; column < grids_number; column++){//当左边缘方块再左一格为空时则可以左移
            k = true;
            for(int j = 0; j < grids_number; j++){
                if(NowGrids[column][0] == NowGrids[j][0] && NowGrids[column][1] - 1 == NowGrids[j][1]){//找出非左边缘方块
                    k = false;
                }
            }
            if(k){//当任一左边缘方块再左一格不为空时返回false
                if(grids[NowGrids[column][0] + Nowrow][NowGrids[column][1] + Nowcolumn - 1] != 0)
                    return false;
            }
        }

        return true;
    }

    public void rightShift(){//方块向右移动一格
        if(right()){//当方块能向右移动时则右移
            for(int row = 0; row < grids_number; row++){//将原来的颜色方块清除
                grids[NowGrids[row][0] + Nowrow][NowGrids[row][1] + Nowcolumn] = 0;
            }
            Nowcolumn++;
            for(int row = 0; row < grids_number; row++){//重新绘制颜色方块
                grids[NowGrids[row][0] + Nowrow][NowGrids[row][1] + Nowcolumn] = Grids;
            }
        }
        drawGrids();
    }

    public boolean right(){//判断方块能否向右移动
        boolean k;
        if(Nowcolumn + column_number + column_start==10){//如果方块向右移动到右边界，则返回false
            return false;
        }

        for(int column = 0; column < grids_number; column++){//当右边缘方块再右一格为空时则可以右移
            k = true;
            for(int j = 0; j < grids_number; j++){
                if(NowGrids[column][0] == NowGrids[j][0] && NowGrids[column][1] + 1 == NowGrids[j][1]){//找出非右边缘方块
                    k = false;
                }
            }
            if(k){//当任一右边缘方块再右一格不为空时返回false
                if(grids[NowGrids[column][0] + Nowrow][NowGrids[column][1] + Nowcolumn + 1] != 0)
                    return false;
            }
        }

        return true;
    }

    public void eliminateGrids() {//当有任一行全部填满颜色方块时，消去该行，且所有方格向下移动一格
        boolean k;
        for (int row = 14; row >= 0; row--) {
            k = true;
            for (int column = 0; column < 10; column++) {//判断是否有任一行全部填满颜色方块
                if (grids[row][column] == 0)
                    k = false;
            }
            if (k) {//消去全部填满颜色方块的行，且所有方格向下移动一格
                for (int i = row - 1; i >= 0; i--) {
                    for (int j = 0; j < 10; j++) {
                        grids[i + 1][j] = grids[i][j];
                    }
                }
                for (int n = 0; n < 10; n++) {
                    grids[0][n] = 0;
                }
            }
        }
        drawGrids();
    }

    public boolean gameover(){//判断游戏是否结束，游戏结束返回true
        for(int row = 0; row < grids_number; row++){//新生成的颜色方块覆盖原有的颜色方块则游戏结束
            if(grids[NowGrids[row][0] + Nowrow][NowGrids[row][1] + Nowcolumn] != 0){
                return true;
            }
        }
        return false;
    }

    //以下为为各种颜色各种形状的方块赋予对应的NowGrids、row_number、column_number、Grids、column_start
    public void createRedGrids1(){
        NowGrids=RedGrids1;
        row_number=2;
        column_number=3;
        Grids=1;
        column_start=3;
    }

    public void createRedGrids2(){
        NowGrids=RedGrids2;
        row_number=3;
        column_number=2;
        Grids=1;
        column_start=4;
    }

    public void createGreenGrids1(){
        NowGrids=GreenGrids1;
        row_number=2;
        column_number=3;
        Grids=2;
        column_start=3;
    }

    public void createGreenGrids2(){
        NowGrids=GreenGrids2;
        row_number=3;
        column_number=2;
        Grids=2;
        column_start=4;
    }

    public void createCyanGrids1(){
        NowGrids=CyanGrids1;
        row_number=4;
        column_number=1;
        Grids=3;
        column_start=4;
    }

    public void createCyanGrids2(){
        NowGrids=CyanGrids2;
        row_number=1;
        column_number=4;
        Grids=3;
        column_start=3;
    }

    public void createMagentaGrids1(){
        NowGrids=MagentaGrids1;
        row_number=2;
        column_number=3;
        Grids=4;
        column_start=3;
    }

    public void createMagentaGrids2(){
        NowGrids=MagentaGrids2;
        row_number=3;
        column_number=2;
        Grids=4;
        column_start=4;
    }

    public void createMagentaGrids3(){
        NowGrids=MagentaGrids3;
        row_number=2;
        column_number=3;
        Grids=4;
        column_start=3;
    }

    public void createMagentaGrids4(){
        NowGrids=MagentaGrids4;
        row_number=3;
        column_number=2;
        Grids=4;
        column_start=4;
    }

    public void createBlueGrids1(){
        NowGrids=BlueGrids1;
        row_number=2;
        column_number=3;
        Grids=5;
        column_start=3;
    }

    public void createBlueGrids2(){
        NowGrids=BlueGrids2;
        row_number=3;
        column_number=2;
        Grids=5;
        column_start=4;
    }

    public void createBlueGrids3(){
        NowGrids=BlueGrids3;
        row_number=2;
        column_number=3;
        Grids=5;
        column_start=3;
    }

    public void createBlueGrids4(){
        NowGrids=BlueGrids4;
        row_number=3;
        column_number=2;
        Grids=5;
        column_start=4;
    }

    public void createWhiteGrids1(){
        NowGrids=WhiteGrids1;
        row_number=2;
        column_number=3;
        Grids=6;
        column_start=3;
    }

    public void createWhiteGrids2(){
        NowGrids=WhiteGrids2;
        row_number=3;
        column_number=2;
        Grids=6;
        column_start=4;
    }

    public void createWhiteGrids3(){
        NowGrids=WhiteGrids3;
        row_number=2;
        column_number=3;
        Grids=6;
        column_start=3;
    }

    public void createWhiteGrids4(){
        NowGrids=WhiteGrids4;
        row_number=3;
        column_number=2;
        Grids=6;
        column_start=4;
    }

    public void createYellowGrids(){
        NowGrids=YellowGrids;
        row_number=2;
        column_number=2;
        Grids=7;
        column_start=4;
    }
    //以上为为各种颜色各种形状的方块赋予对应的NowGrids、row_number、column_number、Grids、column_start的值

    //以下为变换方块形状时调用新方块的"create+Color+Grids"函数，为新方块赋予对应的NowGrids、row_number、column_number、Grids、column_start的值
    public void changRedGrids(){
        if(NowGrids==RedGrids1){
            createRedGrids2();
        }
        else if(NowGrids==RedGrids2){
            createRedGrids1();
        }
    }

    public void changeGreenGrids(){
        if(NowGrids==GreenGrids1){
            createGreenGrids2();
        }
        else if(NowGrids==GreenGrids2){
            createGreenGrids1();
        }
    }

    public void changeCyanGrids(){
        if(NowGrids==CyanGrids1){
            createCyanGrids2();
        }
        else if(NowGrids==CyanGrids2){
            createCyanGrids1();
        }
    }

    public void changeMagentaGrids(){
        if(NowGrids==MagentaGrids1){
            createMagentaGrids2();
        }
        else if(NowGrids==MagentaGrids2){
            createMagentaGrids3();
        }
        else if(NowGrids==MagentaGrids3){
            createMagentaGrids4();
        }
        else if(NowGrids==MagentaGrids4){
            createMagentaGrids1();
        }
    }

    public void changeBlueGrids(){
        if(NowGrids==BlueGrids1){
            createBlueGrids2();
        }
        else if(NowGrids==BlueGrids2){
            createBlueGrids3();
        }
        else if(NowGrids==BlueGrids3){
            createBlueGrids4();
        }
        else if(NowGrids==BlueGrids4){
            createBlueGrids1();
        }
    }

    public void changeWhiteGrids(){
        if(NowGrids==WhiteGrids1){
            createWhiteGrids2();
        }
        else if(NowGrids==WhiteGrids2){
            createWhiteGrids3();
        }
        else if(NowGrids==WhiteGrids3){
            createWhiteGrids4();
        }
        else if(NowGrids==WhiteGrids4){
            createWhiteGrids1();
        }
    }
    //以上为变换方块形状时调用新方块的"create+Color+Grids"函数，为新方块赋予对应的NowGrids、row_number、column_number、Grids、column_start的值

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}
