package rail.mini_project.projectrail;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Input extends AppCompatActivity {
    EditText num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        num= (EditText) findViewById(R.id.editText);
        Button button = (Button) findViewById(R.id.button5);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String input = num.getText().toString();
                if (input.isEmpty()) {
                    num.setError("Enter train number");
                } else {
                    Log.e("Train Number ", input);
                    Intent intentMain = new Intent(Input.this,
                            Route.class);
                    intentMain.putExtra("Train_Number", input);
                    Input.this.startActivity(intentMain);
                }
            }
        });
    }

}
