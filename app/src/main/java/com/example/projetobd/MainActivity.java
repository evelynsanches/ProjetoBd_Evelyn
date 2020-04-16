package com.example.projetobd;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends Activity {


    // Nome do banco que será manipulado
    String nomeBanco = "cadastro";
    // variável para manipular codigo sql
    SQLiteDatabase bancoDados = null;
    EditText nomePessoa;
    EditText fonePessoa;
    ListView mostraDados;
    Cursor cursor; // para manipular os dados
    // lista a ser preenchida
    SimpleCursorAdapter adapterLista;
    public static final String NOMEPESSOA = "nomepessoa";
    private int idClicado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nomePessoa = (EditText) findViewById(R.id.txtnome);
        fonePessoa = (EditText) findViewById(R.id.txtfone);

        criarBanco();
        carregaDado();

        mostraDados.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {

                setarDados(position + 1);
                idClicado = position + 1;
            }
        });

    }// fim do método onCreate

    @SuppressWarnings("deprecation")
    public void setarDados(int position) {
        try {
            bancoDados = openOrCreateDatabase(nomeBanco, MODE_WORLD_READABLE,
                    null);

            Cursor c = bancoDados.rawQuery("select * from "
                    + "tbcadastropessoa where _id = '" + position + "'", null);

            while (c.moveToNext()) {
                nomePessoa.setText(c.getString(c.getColumnIndex("nomepessoa")));
                fonePessoa.setText(c.getString(c.getColumnIndex("fonepessoa")));
            }
        } catch (Exception erro) {
            mensagemAlerta("Erro: " + erro);
        }
    }// fim do método setarDados

    @SuppressWarnings("deprecation")
    public void criarBanco() {
        try {
            bancoDados = openOrCreateDatabase(nomeBanco, MODE_WORLD_READABLE,
                    null);
            String sql = "CREATE TABLE IF NOT EXISTS " + " tbcadastropessoa "
                    + " (_id INTEGER PRIMARY KEY, " + " nomepessoa TEXT, "
                    + " fonepessoa TEXT)";
            bancoDados.execSQL(sql);
            mensagemAlerta("Banco criado/aberto com sucesso!");
        } catch (SQLException e) {
            mensagemAlerta("Erro no banco!" + e);
        }// fim do cath
    }// fim do método criarBanco

    @SuppressWarnings("deprecation")
    public void gravarBanco() {
        try {
            bancoDados = openOrCreateDatabase(nomeBanco, MODE_WORLD_READABLE,
                    null);
            String sql = "INSERT INTO tbcadastropessoa "
                    + "(nomepessoa, fonepessoa) " + "VALUES('"
                    + nomePessoa.getText().toString() + "', " + "'"
                    + fonePessoa.getText().toString() + "')";

            bancoDados.execSQL(sql);
            mensagemAlerta("Gravado com sucesso!");
        } catch (SQLException e) {
            mensagemAlerta("Erro ao gravar." + e);
        }// fim do catch
    }// fim do método criarBanco

    public void gravar(View v) {
        gravarBanco();
        carregaDado();
    }

    @SuppressWarnings("deprecation")
    public void carregaDado() {
        mostraDados = (ListView) findViewById(R.id.mostraDados);

        if (verificaRegistro()) {
            String[] coluna = new String[]{NOMEPESSOA};
            adapterLista = new SimpleCursorAdapter(this, R.layout.mostrabanco,
                    cursor, coluna, new int[]{R.id.carregaDado});
            mostraDados.setAdapter(adapterLista);
        } else {
            mensagemAlerta("Não ha registros");
            mostraDados.setAdapter(null);
        }
    }

    @SuppressWarnings("deprecation")
    private boolean verificaRegistro() {
        boolean verifica = false;

        try {
            bancoDados = openOrCreateDatabase(nomeBanco, MODE_WORLD_READABLE,
                    null);
            cursor = bancoDados
                    .rawQuery("SELECT * FROM tbcadastropessoa", null);

            if (cursor.getCount() != 0) {
                cursor.moveToNext();
                verifica = true;
            } else {
                verifica = false;
            }
        } catch (Exception e) {
            mensagemAlerta("Erro de verificação de registro");
        }

        return verifica;
    }

    public void mensagemAlerta(String msg) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Banco de Dados").setMessage(msg)
                .setNeutralButton("OK", null).show();
    }// fim do método mensagemAlerta

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void excluir(View view) {
        excluirBanco();
        carregaDado();
    }

    private void excluirBanco() {
        try {
            bancoDados = openOrCreateDatabase(nomeBanco, MODE_WORLD_READABLE,
                    null);
            String sql = "DELETE FROM tbcadastropessoa WHERE _id = '" + idClicado + "'";

            bancoDados.execSQL(sql);
            mensagemAlerta("Excluido com sucesso!");
        } catch (SQLException e) {
            mensagemAlerta("Erro ao excluir." + e);
        }// fim do catch
    }

    public void alterar(View view) {
        alterarBanco();
        carregaDado();
    }

    private void alterarBanco() {
        try {
            bancoDados = openOrCreateDatabase(nomeBanco, MODE_WORLD_READABLE,
                    null);
            String sql = "UPDATE tbcadastropessoa " +
                    "set nomePessoa = '" + nomePessoa.getText().toString() + "', " +
                    "fonePessoa = '" + nomePessoa.getText().toString() + "' " +
                    "WHERE _id = '" + idClicado + "'";

            bancoDados.execSQL(sql);
            mensagemAlerta("Alterado com sucesso!");
        } catch (SQLException e) {
            mensagemAlerta("Erro ao alterar." + e);
        }// fim do catch

    }
}
