package com.example.trabalho1bimestre;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText etNomeCliente, etCpfCliente, etItem, etQuantidade, etValorUnitario, etQuantidadeParcelas;
    private TextView tvItensPedido, tvInfoPedido, tvParcelas;
    private Button btnAdicionarItem, btnConcluirPedido, btnCalcularPedido;
    private Spinner spinnerCondicaoPagamento;
    private List<String> listaItens;
    private double valorTotalPedido = 0.0;
    private int totalItensPedido = 0;
    private DecimalFormat df = new DecimalFormat("#.##");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        listaItens = new ArrayList<>();

        btnAdicionarItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adicionarItem();
            }
        });

        btnConcluirPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calcularPedido();
            }
        });

        btnCalcularPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                concluirPedido();
            }
        });

        spinnerCondicaoPagamento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (position == 2) {
                    etQuantidadeParcelas.setVisibility(View.VISIBLE);
                    tvParcelas.setVisibility(View.VISIBLE);
                } else {
                    etQuantidadeParcelas.setVisibility(View.GONE);
                    tvParcelas.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
    private void initViews() {
        etNomeCliente = findViewById(R.id.etNomeCliente);
        etCpfCliente = findViewById(R.id.etCpfCliente);
        etItem = findViewById(R.id.etItem);
        etQuantidade = findViewById(R.id.etQuantidade);
        etValorUnitario = findViewById(R.id.etValorUnitario);
        etQuantidadeParcelas = findViewById(R.id.etQuantidadeParcelas);
        tvItensPedido = findViewById(R.id.tvItensPedido);
        tvInfoPedido = findViewById(R.id.tvInfoPedido);
        tvParcelas = findViewById(R.id.tvParcelas);
        btnAdicionarItem = findViewById(R.id.btnAdicionarItem);
        btnConcluirPedido = findViewById(R.id.btnConcluirPedido);
        spinnerCondicaoPagamento = findViewById(R.id.spinnerCondicaoPagamento);
        btnCalcularPedido = findViewById(R.id.btnCalcularPedido);
    }
    private void limparCampos() {
        etNomeCliente.setText("");
        etCpfCliente.setText("");
        etItem.setText("");
        etQuantidade.setText("");
        etValorUnitario.setText("");
        tvItensPedido.setText("");
        tvInfoPedido.setText("");
        etQuantidadeParcelas.setText("");
        tvParcelas.setText("");
        spinnerCondicaoPagamento.setSelection(0);
    }
    private void adicionarItem() {
        String item = etItem.getText().toString().trim();
        String quantidadeStr = etQuantidade.getText().toString().trim();
        String valorUnitarioStr = etValorUnitario.getText().toString().trim();

        if (TextUtils.isEmpty(item) || TextUtils.isEmpty(quantidadeStr) || TextUtils.isEmpty(valorUnitarioStr)) {
            Toast.makeText(this, "Preencha todos os campos do item", Toast.LENGTH_SHORT).show();
            return;
        }

        int quantidade = Integer.parseInt(quantidadeStr);
        double valorUnitario = Double.parseDouble(valorUnitarioStr);

        if (quantidade <= 0 || valorUnitario <= 0) {
            Toast.makeText(this, "A quantidade e o valor unitário devem ser maiores que zero", Toast.LENGTH_SHORT).show();
            return;
        }

        double valorTotalItem = quantidade * valorUnitario;
        valorTotalPedido += valorTotalItem;
        totalItensPedido += quantidade;

        String itemStr = item + " - " + quantidade + " x " + df.format(valorUnitario) + " = " + df.format(valorTotalItem);
        listaItens.add(itemStr);
        tvItensPedido.setText(TextUtils.join("\n", listaItens));

        tvInfoPedido.setText("Total de itens: " + totalItensPedido + "\nValor total: " + df.format(valorTotalPedido));

        etItem.setText("");
        etQuantidade.setText("");
        etValorUnitario.setText("");
    }

    private void calcularPedido() {
        String nomeCliente = etNomeCliente.getText().toString().trim();
        String cpfCliente = etCpfCliente.getText().toString().trim();
        int condicaoPagamento = spinnerCondicaoPagamento.getSelectedItemPosition();

        if (TextUtils.isEmpty(nomeCliente) || TextUtils.isEmpty(cpfCliente) || condicaoPagamento == 0 || listaItens.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos e adicione pelo menos um item", Toast.LENGTH_SHORT).show();
            return;
        }

        double valorFinalPedido;
        if (condicaoPagamento == 1) {
            valorFinalPedido = valorTotalPedido * 0.95;
        } else {
            valorFinalPedido = valorTotalPedido * 1.05;
        }
        if (condicaoPagamento == 2) {
            String quantidadeParcelasStr = etQuantidadeParcelas.getText().toString().trim();
            if (TextUtils.isEmpty(quantidadeParcelasStr)) {
                Toast.makeText(this, "Informe a quantidade de parcelas", Toast.LENGTH_SHORT).show();
                return;
            }

            int quantidadeParcelas = Integer.parseInt(quantidadeParcelasStr);
            if (quantidadeParcelas <= 0) {
                Toast.makeText(this, "A quantidade de parcelas deve ser maior que zero", Toast.LENGTH_SHORT).show();
                return;
            }

            double valorParcela = valorFinalPedido / quantidadeParcelas;
            tvParcelas.setText("Valor total: " + df.format(valorFinalPedido) + "\nQuantidade de parcelas: " + quantidadeParcelas + "\nValor por parcela: " + df.format(valorParcela));
        } else {
            tvParcelas.setText("Valor total: " + df.format(valorFinalPedido));
        }
    }
    private void concluirPedido(){
            String nomeCliente = etNomeCliente.getText().toString();
            String cpfCliente = etCpfCliente.getText().toString();
            String itensPedido = tvItensPedido.getText().toString();
            String infoPedido = tvInfoPedido.getText().toString();
            int condicaoPagamento = spinnerCondicaoPagamento.getSelectedItemPosition();
            int quantidadeParcelas = 0;
            String textoParcelas = "";
            String totalPedido = "0"; // Calcule o total do pedido com base nos itens adicionados

            if (condicaoPagamento == 1) { // Posição 1 do spinner é pagamento parcelado
                quantidadeParcelas = Integer.parseInt(etQuantidadeParcelas.getText().toString());
                textoParcelas = "\nQuantidade de parcelas: " + quantidadeParcelas;
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Pedido concluído com sucesso!\n Resumo do Pedido:");
            builder.setMessage("Nome: " + nomeCliente +
                    "\nCPF: " + cpfCliente +
                    "\n\nItens:\n" + itensPedido +
                    "\n\n" + infoPedido +
                    textoParcelas +
                    "\n\nTotal do pedido: R$ " + totalPedido);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    limparCampos();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }

    }
