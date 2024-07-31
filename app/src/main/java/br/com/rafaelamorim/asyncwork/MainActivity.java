package br.com.rafaelamorim.asyncwork;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainActivity extends AppCompatActivity {
    private TextView countdownText;
    private ExecutorService executorService;
    private Future<?> countdownFuture;
    private long countdownTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        countdownText = findViewById(R.id.countdown_text);
        executorService = Executors.newSingleThreadExecutor();
        countdownTime = 10000; // Inicializa countdownTime com 10 segundos
        startCountdown(); // Inicia a contagem regressiva
    }
    private void startCountdown() {
        countdownFuture = executorService.submit(() -> {
            while (countdownTime > 0) {
                long finalCountdownTime = countdownTime;
                runOnUiThread(() -> {
                    countdownText.setText("Tempo Restante: " + (finalCountdownTime / 1000) + " segundos");
                });

                try {
                    Thread.sleep(1000); // Aguarde 1 segundo
                    countdownTime -= 1000; // Subtraia 1 segundo do tempo restante
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            runOnUiThread(() -> {
                countdownText.setText("Contagem Regressiva Concluída");
                Toast.makeText(this, "Contagem Regressiva Concluída", Toast.LENGTH_SHORT).show();
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countdownFuture != null) {
            countdownFuture.cancel(true); // Cancela a tarefa se a atividade for destruída
        }
        executorService.shutdown(); // Encerra o executor service
    }
}