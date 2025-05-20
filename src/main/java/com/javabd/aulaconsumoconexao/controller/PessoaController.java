package com.javabd.aulaconsumoconexao.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javabd.aulaconsumoconexao.model.Pessoa;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class PessoaController {

    @FXML
    private TextField edDataNasc;

    @FXML
    private TextField edCpf;

    @FXML
    private TextField edEndereco;

    @FXML
    private TextField edId;

    @FXML
    private TextField edNome;

    List<Pessoa> pessoas;
    private Pessoa pessoaSelecionada;

    @FXML
    private ListView<String> listaPessoas;

    @FXML
    private Label welcomeText;

    @FXML
    public void initialize() {
        carregarPessoa();
        edId.setEditable(false);
    }

    private void carregarPessoa() {
        try{
            URL url = new URL("http://localhost:8081/pessoa");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder reposta =  new StringBuilder();
            String linha;
            while ((linha = br.readLine()) != null) {
                reposta.append(linha);
            }
            br.close();

            ObjectMapper objectMapper = new ObjectMapper();
            pessoas = objectMapper.readValue(reposta.toString(), new TypeReference<List<Pessoa>>(){});

            ObservableList<String> nomes = FXCollections.observableArrayList();
            for (Pessoa p : pessoas) {
                nomes.add(p.getId()+" - "+p.getNome());
            }
            listaPessoas.setItems(nomes);
        } catch (Exception e) {
            welcomeText.setText("Erro ao carregar a lista: "+e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void selecionarPessoa(MouseEvent event) {
        String selecionado = listaPessoas.getSelectionModel().getSelectedItem();
        if (selecionado != null && pessoas != null) {
            int idSelecionado = Integer.parseInt(selecionado.split(" - ")[0]);
            for (Pessoa p : pessoas) {
                if (p.getId() == idSelecionado) {
                    pessoaSelecionada = p;
                    edId.setText(String.valueOf(p.getId()));
                    edNome.setText(p.getNome());
                    edEndereco.setText(p.getEndereco());
                    edCpf.setText(p.getCpf());
                    edDataNasc.setText(p.getData_nasc());
                    break;
                }
            }
        }
    }

    @FXML
    void btnLimpar(ActionEvent event) {
        limparCampos();
    }

    private void limparCampos(){
        edDataNasc.clear();
        edCpf.clear();
        edEndereco.clear();
        edId.clear();
        edNome.clear();
    }

    @FXML
    void btnExcluir(ActionEvent event) {
        String idTexto = edId.getText();
        if(idTexto.isEmpty() || idTexto == null) {
            welcomeText.setText("Informe um ID para excluir!");
            return;
        }
        try {
            int id = Integer.parseInt(idTexto);
            URL url = new URL("http://localhost:8081/pessoa/"+id);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setDoOutput(true);

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                welcomeText.setText("Pessoa excluída com sucesso!");
                carregarPessoa();
                limparCampos();
            } else {
                welcomeText.setText("Erro ao excluir pessoa!");
            }
            conn.disconnect();
        }catch (Exception e){
            welcomeText.setText("Erro ao excluir o ID: "+e.getMessage());
            e.printStackTrace();
        }

    }

    @FXML
    void btnSalvar(ActionEvent event) {
        try {
            Pessoa p = new Pessoa();
            boolean isEdicao = false;

            if(!edId.getText().isEmpty()) {
                p.setId(Integer.parseInt(edId.getText()));
                isEdicao = true;
            }
            p.setNome(edNome.getText());
            p.setCpf(edCpf.getText());
            p.setEndereco(edEndereco.getText());
            p.setData_nasc(edDataNasc.getText());

            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(p);

            URL url;
            HttpURLConnection conn;

            if(isEdicao) {
                url = new URL("http://localhost:8081/pessoa/"+p.getId());
                conn = (HttpURLConnection)url.openConnection();
                conn.setRequestMethod("PUT");
            } else {
                url = new URL("http://localhost:8081/pessoa/salvar");
                conn = (HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");
            }
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes());
                os.flush();
            }
            int responseCode = conn.getResponseCode();
            if (responseCode == 200 || responseCode == 201) {
                welcomeText.setText(
                        isEdicao
                        ? "Pessoa atualizada com sucesso!"
                        : "Pessoa salva com sucesso!"
                );
                limparCampos();
                carregarPessoa();
            }else {
                welcomeText.setText("Erro ao salvar pessoa! Código: "+responseCode);
            }
            conn.disconnect();
        } catch (Exception e){
            welcomeText.setText("Erro ao salvar pessoa!");
            e.printStackTrace();
        }
    }

}
