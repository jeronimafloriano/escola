package escola.repository;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import escola.codecs.AlunoCodec;
import escola.model.Aluno;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AlunoRepository {

    private MongoCollection<Aluno> alunos;
    private MongoClient client;
    private MongoDatabase database;

    public void criarConexão(){
        Codec<Document> codec = MongoClient.getDefaultCodecRegistry().get(Document.class);
        AlunoCodec alunoCodec = new AlunoCodec(codec);
        CodecRegistry registro = CodecRegistries.fromRegistries(MongoClient.getDefaultCodecRegistry(),
                CodecRegistries.fromCodecs(alunoCodec));
        MongoClientOptions options = MongoClientOptions.builder().codecRegistry(registro).build();

        this.client = new MongoClient("localhost:27017", options);
        this.database = client.getDatabase("Java_Alunos");
        this.alunos = database.getCollection("alunos", Aluno.class);
    }

    public void salvar(Aluno aluno){
        this.criarConexão();
        alunos.insertOne(aluno);
        client.close();
    }

    public List<Aluno> obterTodosAlunos(){
        this.criarConexão();

        MongoCursor<Aluno> resultado = alunos.find().iterator();
        List<Aluno> alunosEncontrados = new ArrayList<>();

        while (resultado.hasNext()){
            Aluno aluno = resultado.next();
            alunosEncontrados.add(aluno);
        }

        this.client.close();
        return alunosEncontrados;
    }

}
