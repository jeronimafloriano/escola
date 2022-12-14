package escola.controllers;

import escola.model.Aluno;
import escola.model.Nota;
import escola.repository.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class NotaController {

    @Autowired
    private AlunoRepository alunoRepository;

    @GetMapping("/nota/cadastrar/{id}")
    public String cadastrar(@PathVariable String id, Model model){
        Aluno aluno = alunoRepository.obterAlunoPorId(id);
        model.addAttribute("aluno", aluno);
        model.addAttribute("nota", new Nota());
        return "/nota/cadastrar";
    }

    @PostMapping("/nota/salvar/{id}")
    public String salvar(@PathVariable String id, @ModelAttribute Nota nota){
        Aluno aluno = alunoRepository.obterAlunoPorId(id);
        alunoRepository.salvar(aluno.adicionar(aluno, nota));
        return "redirect:/aluno/listar";
    }

    @GetMapping("/nota/iniciarpesquisa")
    public String iniciarPesquisa(){
        return "/nota/pesquisar";
    }

    @GetMapping("/nota/pesquisar")
    public String pesquisarPor(@RequestParam("classificacao") String classificacao, @RequestParam("notacorte") String notaCorte, Model model) {
        List<Aluno> alunos = alunoRepository.pesquisaPor(classificacao, Double.parseDouble(notaCorte));
        model.addAttribute("alunos", alunos);
        return "nota/pesquisar";
    }
}
