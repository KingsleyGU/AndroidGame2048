package com.example.game;

import android.content.Context;
import android.content.Intent;
import android.gesture.GestureOverlayView;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.*;
import android.app.Activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class gameActivity extends Activity{
    int[][] viewIds = { { R.id.image1, R.id.image2, R.id.image3, R.id.image4},
            { R.id.image5, R.id.image6, R.id.image7, R.id.image8},
            { R.id.image9, R.id.image10, R.id.image11, R.id.image12},
            { R.id.image13, R.id.image14, R.id.image15, R.id.image16}};

    final String[] images = {"image0.png","image1.png","image2.png","image3.png",
            "image4.png","image5.png","image6.png","image7.png",
            "image8.png", "image9.png"};

    int[][] ArrayShow = { {0,0,0,0},
            {0,0,0,0},
            {0,0,0,0},
            {0,0,0,0} };

    int[][] oldArray = new int[4][4];
    float x;
    float y;
    String username = "";
    private InputStreamReader inputStreamReader;
    private BufferedReader bufferedReader;
    private Socket client1;
    private PrintWriter printwriter;
    private EditText textField;
    private Button button;
    private String message;
    private String reply;
    private int Score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);
        init();
        showResult();
        scoreDetect();
        LinearLayout layout = (LinearLayout) findViewById(R.id.wholeContent);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            username = extras.getString("username");
        }
        TextView userView = (TextView) findViewById(R.id.userName);
        userView.setText("Hello,  " + username);

        button = (Button) findViewById(R.id.submit_to_Server); // reference to the send button
        Button exitBtn = (Button) findViewById(R.id.exit);
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(gameActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        // Button press event listener
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                SendMessage sendMessageTask = new SendMessage();
                sendMessageTask.execute();
            }
        });

        layout.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {

                if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
                    x = arg1.getX();
                    y = arg1.getY();
                } else if (arg1.getAction() == MotionEvent.ACTION_UP) {
                    float CurrentX = arg1.getX();
                    float CurrentY = arg1.getY();
                    float absX = Math.abs(CurrentX - x);
                    float absY = Math.abs(CurrentY - y);
                    System.out.println("11111111" + x + "  " + CurrentX);
                    if (absX >= 150 && absX > absY && CurrentX > x) {
                        moveRight();
                    } else if (absX >= 150 && absX > absY && CurrentX < x) {
                        moveLeft();
                    } else if (absY >= 150 && absX < absY && CurrentY > y) {
                        moveDown();
                    } else if (absY >= 150 && absX < absY && CurrentY < y) {
                        moveUp();
                    }

                }

                return true;
            }
        });
    }
    public int getImage(int num)
    {
        switch(num)
        {
            case 0: return R.drawable.image0;
            case 2: return R.drawable.image1;
            case 4: return R.drawable.image2;
            case 8: return R.drawable.image3;
            case 16: return R.drawable.image4;
            case 32: return R.drawable.image5;
            case 64: return R.drawable.image6;
            case 128: return R.drawable.image7;
            case 256: return R.drawable.image8;
            case 512: return R.drawable.image9;
            case 1024: return R.drawable.image10;
            case 2048: return R.drawable.image11;
        }
        return R.drawable.image0;
    }
    public int RandomNum()
    {
        Random random = new Random();
        if(random.nextInt(4) > 1)
        {
            return 4;
        }
        else{
            return 2;
        }
    }
    public void init()
// initiate the array;
    {
        int[] position = new int[4];
        Random random = new Random();
        for(int m=0; m<4; m++)
        {
            position[m] = random.nextInt(4);
        }
        while(position[0] == position[2]&& position[1] == position[3]){
            position[2] = random.nextInt(4);
            position[3] = random.nextInt(4);
        }
        ArrayShow[(position[0])][(position[1])] = RandomNum();
        ArrayShow[(position[2])][(position[3])] = RandomNum();
    }
    public void addNum()
// add two random numbers to the array;
    {

        int[] position = new int[2];
        Random random = new Random();
        position[0] = random.nextInt(4);
        position[1] = random.nextInt(4);
        int n = 0;
        while(n<1)
        {
            if(ArrayShow[position[0]][position[1]] == 0)
            {
                ArrayShow[position[0]][position[1]] = RandomNum();
                n++;
            }
            else
            {
                position[0] = random.nextInt(4);
                position[1] = random.nextInt(4);
            }
        }

    }
    public void moveLeft()
    {
        int[][] tempArray =  { {0,0,0,0},
                {0,0,0,0},
                {0,0,0,0},
                {0,0,0,0} };
        for(int m=0; m<4; m++)
        {
            int n = 0;
            int current = 0;
            boolean flag = false;
            while(n<4)
            {
                if(n==0&&ArrayShow[m][n]!=0)
                {
                    flag = true;
                    tempArray[m][current] = ArrayShow[m][n];
                }
                else if(flag&& ArrayShow[m][n]==tempArray[m][current])
                {
                    flag = false;
                    tempArray[m][current] = tempArray[m][current]*2;
                    current ++;
                }
                else if(flag&& ArrayShow[m][n]!=0&& ArrayShow[m][n]!=tempArray[m][current])
                {
                    current ++;
                    tempArray[m][current] = ArrayShow[m][n];

                }
                else if((!flag) && ArrayShow[m][n]!=0)
                {
                    tempArray[m][current] = ArrayShow[m][n];
                    flag = true;
                }
                n++;
            }
        }
        boolean isSame = compareResult(tempArray);
        for(int x=0; x<4 ;x++)
        {
            for(int y=0; y<4; y++)
            {
                ArrayShow[x][y] = tempArray[x][y];
            }
        }
        showResult();
        int isEnd = endDetect();
        if(isSame&&isEnd == 0)
            addNum();
        showResult();
        scoreDetect();
    }
    public void moveUp()
    {
        int[][] tempArray =  { {0,0,0,0},
                {0,0,0,0},
                {0,0,0,0},
                {0,0,0,0} };
        for(int m=0; m<4; m++)
        {
            int n = 0;
            int current = 0;
            boolean flag = false;
            while(n<4)
            {
                if(n==0&&ArrayShow[n][m]!=0)
                {
                    flag = true;
                    tempArray[current][m] = ArrayShow[n][m];
                }
                else if(flag&& ArrayShow[n][m]==tempArray[current][m] )
                {
                    flag = false;
                    tempArray[current][m] = tempArray[current][m]*2;
                    current ++;
                }
                else if(flag&& ArrayShow[n][m]!=0&& ArrayShow[n][m]!=tempArray[current][m])
                {
                    current ++;
                    tempArray[current][m] = ArrayShow[n][m];

                }
                else if((!flag) && ArrayShow[n][m]!=0)
                {
                    tempArray[current][m] = ArrayShow[n][m];
                    flag = true;
                }
                n++;
            }
        }
        boolean isSame = compareResult(tempArray);
        for(int x=0; x<4 ;x++)
        {
            for(int y=0; y<4; y++)
            {
                ArrayShow[x][y] = tempArray[x][y];
            }
        }
        showResult();
        int isEnd = endDetect();
        if(isSame&&isEnd == 0)
            addNum();
        showResult();
        scoreDetect();
    }
    public void moveRight()
    {
        int[][] tempArray =  { {0,0,0,0},
                {0,0,0,0},
                {0,0,0,0},
                {0,0,0,0} };
        for(int m=0; m<4; m++)
        {
            int n = 3;
            int current = 3;
            boolean flag = false;
            while(n>=0)
            {
                if(n==3&&ArrayShow[m][n]!=0)
                {
                    flag = true;
                    tempArray[m][current] = ArrayShow[m][n];
                }
                else if(flag&& ArrayShow[m][n]==tempArray[m][current])
                {
                    flag = false;
                    tempArray[m][current] = tempArray[m][current]*2;
                    current --;
                }
                else if(flag&& ArrayShow[m][n]!=0&& ArrayShow[m][n]!=tempArray[m][current])
                {
                    current --;
                    tempArray[m][current] = ArrayShow[m][n];

                }
                else if((!flag) && ArrayShow[m][n]!=0)
                {
                    tempArray[m][current] = ArrayShow[m][n];
                    flag = true;
                }
                n--;
            }
        }
        boolean isSame = compareResult(tempArray);
        for(int x=0; x<4 ;x++)
        {
            for(int y=0; y<4; y++)
            {
                ArrayShow[x][y] = tempArray[x][y];
            }
        }
        showResult();
        int isEnd = endDetect();
        if(isSame&&isEnd == 0)
            addNum();
        showResult();
        scoreDetect();
    }
    public void moveDown()
    {
        int[][] tempArray =  { {0,0,0,0},
                {0,0,0,0},
                {0,0,0,0},
                {0,0,0,0} };
        for(int m=0; m<4; m++)
        {
            int n = 3;
            int current = 3;
            boolean flag = false;
            while(n>=0)
            {
                if(n==3&&ArrayShow[n][m]!=0)
                {
                    flag = true;
                    tempArray[current][m] = ArrayShow[n][m];
                }
                else if(flag&& ArrayShow[n][m]==tempArray[current][m] )
                {
                    flag = false;
                    tempArray[current][m] = tempArray[current][m]*2;
                    current --;
                }
                else if(flag&& ArrayShow[n][m]!=0&& ArrayShow[n][m]!=tempArray[current][m])
                {
                    current --;
                    tempArray[current][m] = ArrayShow[n][m];

                }
                else if((!flag) && ArrayShow[n][m]!=0)
                {
                    tempArray[current][m] = ArrayShow[n][m];
                    flag = true;
                }
                n--;
            }
        }
        boolean isSame = compareResult(tempArray);
        for(int x=0; x<4 ;x++)
        {
            for(int y=0; y<4; y++)
            {
                ArrayShow[x][y] = tempArray[x][y];
            }
        }
        showResult();
        int isEnd = endDetect();
        if(isSame&&isEnd == 0)
            addNum();
        showResult();
        scoreDetect();
    }
    public boolean compareResult(int[][] tempArray)
    {
        for(int m=0; m<4; m++)
        {
            for(int n=0; n<4; n++)
            {
                if(tempArray[m][n]!=ArrayShow[m][n])
                {
                    return true;
                }
            }
        }
        return false;

    }
    public void showResult()
    {
        for(int i = 0; i<4; i++)
        {
            for(int j=0; j<4; j++)
            {

                ImageView imgView = (ImageView) findViewById(viewIds[i][j]);
                imgView.setImageResource(getImage(ArrayShow[i][j]));

            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void scoreDetect()
    {
        for(int m=0 ; m<4 ;m++)
        {
            for(int n=0; n<4 ;n++)
            {
               if(Score<ArrayShow[m][n])
               {
                   Score = ArrayShow[m][n];
               }
            }
        }
    }
    public int endDetect()
    {
        for(int m = 0; m<4 ;m++)
        {
            for(int n=0; n<4; n++)
            {
                if((m<3&&ArrayShow[m][n]==ArrayShow[m+1][n])||(n<3&&ArrayShow[m][n]==ArrayShow[m][n+1]))
                    return 0;
                else if(ArrayShow[m][n] == 2048)
                {
                    Context context = getApplicationContext();
                    CharSequence text = "You win!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    return 2;
                }
                else if(ArrayShow[m][n] == 0)
                    return 0;
            }
        }
        Context context = getApplicationContext();
        CharSequence text = "u lose!";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        return 1;
    }
    private class SendMessage extends AsyncTask<Void, Void, Void> {
        public String readResult="sacdsadcsa";
        @Override
        protected Void doInBackground(Void... params) {
            String result = "";
            String line="";
            try {

                client1 = new Socket("10.0.2.2", 7000); // connect to the server
                printwriter = new PrintWriter(client1.getOutputStream(), true);
                message = username + ":"+Score;
                printwriter.println(message); // write the message to output stream

                printwriter.flush();
//                printwriter.close();
                inputStreamReader = new InputStreamReader(client1.getInputStream());
                bufferedReader = new BufferedReader(inputStreamReader); // get the client message

                while (( line=bufferedReader.readLine()) != null) {
                    line="hahahahaha";
                }
//                inputStreamReader.close();
                client1.close(); // closing the connection
                reply = "hahdhjajhhjad";

            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

    }


}

