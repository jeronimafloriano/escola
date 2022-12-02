package escola.repository;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import escola.codecs.AlunoCodec;
import escola.model.Aluno;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AlunoRepository {

    private MongoCollection<Aluno> alunos;
    private MongoClient client;
    private MongoDatabase database;

    private void criarConexão(){
        Codec<Document> codec = MongoClient.getDefaultCodecRegistry().get(Document.class);
        AlunoCodec alunoCodec = new AlunoCodec(codec);
        CodecRegistry registro = CodecRegistries.fromRegistries(MongoClient.getDefaultCodecRegistry(),
                CodecRegistries.fromCodecs(alunoCodec));
        MongoClientOptions options = MongoClientOptions.builder()
                .codecRegistry(registro)
                .serverSelectionTimeout(3000000)
                .build();

        this.client = new MongoClient("localhost:27017", options);
        this.database = client.getDatabase("Java_Alunos");
        this.alunos = database.getCollection("alunos", Aluno.class);
    }

    public void salvar(Aluno aluno){
        this.criarConexão();
        if(aluno.getId() == null){
            alunos.insertOne(aluno);
        } else {
            alunos.updateOne(Filters.eq("_id", aluno.getId()), new Document("$set", aluno));
        }
        fecharConexao();
    }

    public List<Aluno> obterTodosAlunos(){
        this.criarConexão();

        MongoCursor<Aluno> resultado = alunos.find().iterator();
        List<Aluno> alunosEncontrados = popularAlunos(resultado);
        fecharConexao();

        return alunosEncontrados;
    }

    public Aluno obterAlunoPorId(String id) {
        this.criarConexão();
        Aluno aluno = alunos.find(Filters.eq("_id", new ObjectId(id))).first();
        fecharConexao();
        return aluno;
    }

    public List<Aluno> pesquisarPor(String nome){
        this.criarConexão();

        MongoCursor<Aluno> resultados = alunos
                .find(Filters.eq("nome", nome), Aluno.class).iterator();

        List<Aluno> alunos = popularAlunos(resultados);
        fecharConexao();

        return alunos;
    }

    private List<Aluno> popularAlunos( MongoCursor<Aluno> resultados){
        List<Aluno> alunosEncontrados = new ArrayList<>();

        while (resultados.hasNext()){
            Aluno aluno = resultados.next();
            alunosEncontrados.add(aluno);
        }
        return alunosEncontrados;
    }

    private void fecharConexao(){
        this.client.close();
    }
}
