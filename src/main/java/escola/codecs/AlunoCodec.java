package escola.codecs;

import escola.model.Aluno;
import escola.model.Curso;
import org.bson.*;
import org.bson.codecs.Codec;
import org.bson.codecs.CollectibleCodec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.types.ObjectId;

import java.util.Date;

public class AlunoCodec implements CollectibleCodec<Aluno> {

    private Codec<Document> codec;

    public AlunoCodec(Codec<Document> codec) {
        this.codec = codec;
    }

    @Override
    public void encode(BsonWriter writer, Aluno aluno, EncoderContext encoder) {
        ObjectId id = aluno.getId();
        String nome = aluno.getNome();
        Date dataNascimento = aluno.getDataNascimento();
        Curso curso = aluno.getCurso();

        Document document = new Document();

        document.put("_id", id);
        document.put("nome", nome);
        document.put("data_nascimento", dataNascimento);
        document.put("curso", new Document("nome", curso.getNome()));

        this.codec.encode(writer, document, encoder);
    }

    @Override
    public Class<Aluno> getEncoderClass() {
        return Aluno.class;
    }

    @Override
    public boolean documentHasId(Aluno aluno) {
        return aluno.getId() != null;
    }

    @Override
    public Aluno generateIdIfAbsentFromDocument(Aluno aluno) {
        return documentHasId(aluno) ? aluno : aluno.criarId();
    }

    @Override
    public BsonValue getDocumentId(Aluno aluno) {
        if(!documentHasId(aluno)){
            throw new IllegalStateException("Esse documento não possui Id.");
        }
        return new BsonString(aluno.getId().toHexString());
    }

    @Override
    public Aluno decode(BsonReader reader, DecoderContext decoder) {
        Document document = this.codec.decode(reader, decoder);

        Aluno aluno = new Aluno();

        aluno.setId(document.getObjectId("_id"));
        aluno.setNome(document.getString("nome"));
        aluno.setDataNascimento(document.getDate("data_nascimento"));
        Document curso = (Document) document.get("curso");
        if (curso != null){
            String nomeCurso = curso.getString("curso");
            aluno.setCurso(new Curso(nomeCurso));
        }
        return aluno;
    }




}
