package ro.pub.cs.systems.eim.practicaltest02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PracticalTest02MainActivity extends AppCompatActivity {

    // Server widgets
    private EditText serverPortEditText = null;
    private Button connectButton = null;

    // Client widgets
    private EditText clientAddressEditText = null;
    private EditText clientPortEditText = null;
    private EditText wordEditText = null;
    private EditText lengthEditText = null;
    private Button getButton = null;
    private TextView infoTextView = null;


    private ServerThread serverThread = null;
    private ClientThread clientThread = null;

    private ConnectButtonClickListener connectButtonClickListener = new ConnectButtonClickListener();
    private class ConnectButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String serverPort = serverPortEditText.getText().toString();
            if (serverPort == null || serverPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Server port should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            serverThread = new ServerThread(Integer.parseInt(serverPort));
            if (serverThread.getServerSocket() == null) {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not create server thread!");
                return;
            }

            serverThread.start();
        }

    }

    private GetButtonClickListener getButtonClickListener = new GetButtonClickListener();
    private class GetButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String clientAddress = clientAddressEditText.getText().toString();
            String clientPort = clientPortEditText.getText().toString();
            if (clientAddress == null || clientAddress.isEmpty()
                    || clientPort == null || clientPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Client connection parameters should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (serverThread == null || !serverThread.isAlive()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                return;
            }
            String word = wordEditText.getText().toString();
            if (word == null || word.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Word parameter should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            String length = lengthEditText.getText().toString();
            if (length == null || length.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Length parameter should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }


            infoTextView.setText(Constants.EMPTY_STRING);

            clientThread = new ClientThread(
                    clientAddress, Integer.parseInt(clientPort), word, length, infoTextView
            );
            clientThread.start();
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);

        serverPortEditText = (EditText) findViewById(R.id.server_port_edit_text);
        connectButton = (Button) findViewById(R.id.connect_button);
        connectButton.setOnClickListener(connectButtonClickListener);

        clientAddressEditText = (EditText) findViewById(R.id.server_address_edit_text);
        clientPortEditText = (EditText) findViewById(R.id.server_port_from_client);
        wordEditText = (EditText) findViewById(R.id.word_edit_text);
        lengthEditText = (EditText) findViewById(R.id.length_edit_text);
        getButton = (Button) findViewById(R.id.get_button);
        getButton.setOnClickListener(getButtonClickListener);
        infoTextView = (TextView) findViewById(R.id.info_text_view);

    }


    @Override
    protected void onDestroy() {
        Log.i(Constants.TAG, "[MAIN ACTIVITY] onDestroy() callback method has been invoked");
        if (serverThread != null) {
            serverThread.stopThread();
        }
        super.onDestroy();
    }
}
