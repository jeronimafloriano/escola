package escola;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Principal {

    /*
    MongoClientURI connectionString = new MongoClientURI("mongodb://localhost:27017");
    MongoClient mongoClient = new MongoClient(connectionString);
    CodecRegistry pojoCodecRegistry = org.bson.codecs.configuration.CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), org.bson.codecs.configuration.CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));
    MongoDatabase database = mongoClient.getDatabase("testdb").withCodecRegistry(pojoCodecRegistry);
     */

    public static void main(String[] args) {

        MongoClient cliente = new MongoClient();
        MongoDatabase banco = cliente.getDatabase("Java_Alunos");
        MongoCollection<Document> alunos = banco.getCollection("alunos");
        Document aluno = alunos.find().first();
        System.out.println(aluno);


        /*Document novoAluno = new Document("nome", "João da Silva")
                .append("data_nascimento", new Date(2003,12,28))
                .append("curso", new Document("nome", "Matemática"))
                .append("notas", Arrays.asList(9,8,10))
                .append("habilidades",
                        Arrays.asList(new Document()
                                                .append("nome", "Inglês")
                                                .append("nível", "Intermediário"),
                                        new Document()
                                                .append("nome", "Francês")
                                                .append("nível", "Básico")));

        alunos.insertOne(novoAluno);*/

        /*alunos.updateOne(
                Filters.eq("nome", "João da Silva"),
                new Document("$set", new Document("nome", "João da Silva Santos")));*/

        alunos.deleteOne(Filters.eq("nome", "João da Silva Santos"));


        cliente.close();

    }
}
