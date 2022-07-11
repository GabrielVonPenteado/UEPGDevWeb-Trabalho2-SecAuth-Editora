package dw.editora.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dw.editora.model.Artigo;
import dw.editora.repository.ArtigoRepository;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

@CrossOrigin(origins = "http://localost:8080")

//Notação de controlador e todos que seguirem o modelo /api serão destinado para cá
@RestController
@RequestMapping("/api")
public class ArtigoController {
    //Precisamos colocar dentro do controlado o repository
    //Notação que faz com que consigamos fazer meu framework criar um objeto e injetar no codigo
    @Autowired
    ArtigoRepository rep;

    //Criar metodos que respondam a api

    //Lista todos os artigos ao seguir o modelo /artigos (get)
    //ResponseEntity -> encapsula um objeto que será retornado a camada de visualização
    //Os métodos devem retornar uma lista de artigos
    //Parâmetro titulo será para buscar por título(requestparam false para ser opcional)
    @GetMapping("/artigos")
    public ResponseEntity<List<Artigo>> getAllArtigos(@RequestParam(required = false) String titulo) {
        try{
                List<Artigo> la = new ArrayList<Artigo>();
                
                //Se nao esta passando titulo, adiciona a lista e retorna todos os artigos encontrados no banco de dados
                if (titulo == null) {
                    rep.findAll().forEach(la::add);
                }else {
                    //Se estiver passando, ache por titulo e adicione a lista
                    rep.findByTituloContaining(titulo).forEach(la::add);
                }

                //Se a busca nao retornou nada, erro de sem conteudo
                if(la.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }else {
                    //Se retornou algo, retornamos a lista e um ok
                    return new ResponseEntity<>(la, HttpStatus.OK);
                }
        }catch (Exception e) {
            //Se nao der boa a busca, não retornamos nenhum objeto e retornamos um erro (httpstatuses.com)
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Criar artigo (post)
    @PostMapping("/artigos")
    public ResponseEntity<Artigo> createArtigo(@RequestBody Artigo ar) {
        try{
            //Cria um objeto do tipo artigo, que é criado pelo save
            //No save criamos um Artigo, para termos um controle de identificação dentro do hibernate
            //Pegando sey titulo, resumo e publicado
            //Com o save persistimos na base de dados, quando persistir retornamos um objeto ja com o ID criado
            Artigo _a = rep.save(new Artigo(ar.getTitulo(), ar.getResumo(), ar.isPublicado()));

            //Retorna o artigo criado e um status de criado
            return new ResponseEntity<>(_a, HttpStatus.CREATED);

        }catch (Exception e) {
            //Se nao der boa a criaçao, não retornamos nenhum objeto e retornamos um erro (httpstatuses.com)
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Listar artigo por ID (get)
    //Usuario passa o id no link
    @GetMapping("/artigos/{id}")
    public ResponseEntity<Artigo> getArtigoById(@PathVariable("id") long id) {
        //Optional outra opçao para List
        //Faz a busca e passa o id caso ele ache
        Optional<Artigo> data = rep.findById(id);

        //Se ele achou o id
        if(data.isPresent()) {
            //Retornamos o id achado e retorna ok
            return new ResponseEntity<>(data.get(), HttpStatus.OK);
        }else {
            //Se nao achou retorna um not found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //Atualizar id (put)
    @PutMapping("/artigos/{id}")
    public ResponseEntity<Artigo> updateArtigo(@PathVariable("id") long id, @RequestBody Artigo a) {
        //Obter o id do banco
        Optional<Artigo> data = rep.findById(id);

        //Se existir altera o atributos dele e os do artigo que passams por parametro

        if(data.isPresent()) {
            //
            Artigo ar = data.get();
            ar.setPublicado(a.isPublicado());
            ar.setResumo(a.getResumo());
            ar.setTitulo(a.getTitulo());

            //Salva o objeto e retorna OK
            return new ResponseEntity<>(rep.save(ar), HttpStatus.OK);
        }else {
            //Caso nao exista retorna NOTFOUND
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //Deletar artigo por um id
    @DeleteMapping("/artigos/{id}")
    public ResponseEntity<HttpStatus> deleteArtigo(@PathVariable("id") long id) {
        try{
            //Chama o metodo e deleta o id
            rep.deleteById(id);
            //Retorna q esta sem conteudo
            return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
        }catch(Exception e) {
            //Se n existir o objeto retorna erro
            return new ResponseEntity<HttpStatus>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Deletar todos
    @DeleteMapping("/artigos")
    public ResponseEntity<HttpStatus> deleteAllArtigo() {
        try{
            //Chama o metodo e deleta tdos
            rep.deleteAll();
            //Retorna q esta sem conteudo
            return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
        }catch(Exception e) {
            //Se n existir o objeto retorna erro
            return new ResponseEntity<HttpStatus>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}