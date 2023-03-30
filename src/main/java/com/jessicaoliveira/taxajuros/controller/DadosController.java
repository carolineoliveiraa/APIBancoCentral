package com.jessicaoliveira.taxajuros.controller;

import com.jessicaoliveira.taxajuros.model.DadosModel;
import com.jessicaoliveira.taxajuros.model.ResponseDadosModel;
import com.jessicaoliveira.taxajuros.response.ErrorResponse;
import com.jessicaoliveira.taxajuros.service.DadosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController 
@Validated // Validacoes nos parametros dos metodos
@RequiredArgsConstructor // cria construtor
@RequestMapping("api") // mapeamento
public class DadosController {

    //Injecao de dependencia, e o lombok é utilizado aqui
    private final DadosService dadosService;

    //Operações CRUD
    //ResponseEntity é uma classe do Spring Framework que representa uma resposta HTTP.

    @Tag(name = "Consultar por Id")
    @Operation(summary = "Retorna os dados a partir do Id informado.")
    @GetMapping("/buscar/{id}")
    //Objeto criado ResponseEntity, o ? indica que o retorno é generico e vai ser conhecido em tempo de execução
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        //Optional = um valor pode ou nao estar presente
        Optional<DadosModel> dadosOptional = dadosService.buscarPorId(id);
        //Se o objeto Optional estiver presente, retorna 200 ok
        if (dadosOptional.isPresent()) {
            return ResponseEntity.ok(dadosOptional.get());
        //Senao cria um objeto erroResponse com uma mensagem, e retornada no corpo da resposta(body)
        } else {
            ErrorResponse error = new ErrorResponse("Dados não encontrados para o id: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @Tag(name = "Consultar tudo")
    @Operation(summary = "Consulta de todos os dados da API.")
    @GetMapping("/consultarTodos")
    // Objeto response com tipo generico ResponseDadosModel e metodo ListarTodos
    public ResponseEntity<ResponseDadosModel> listarTodos() {
        // chama o serviço para obter uma lista de dados
        List<DadosModel> dados = dadosService.listarTodos();
        // retorna uma resposta HTTP 200 OK com a lista de dados no corpo da resposta
        return ResponseEntity.ok(new ResponseDadosModel(dados));
    }

    @Tag(name = "Salvar")
    @Operation(summary = "Salvamento de novos dados.")
    @PostMapping("/salvar")
    public ResponseEntity<?> criar(@Valid @RequestBody DadosModel dadosModel, BindingResult result) {
        // verifica se existem erros de validação no objeto "dadosModel"
        if (result.hasErrors()) {
            // caso existam, retorna uma resposta HTTP 400 BAD REQUEST com uma mensagem de erro
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro na validação dos dados");
        }
        // caso não existam erros, cria e salva o objeto "dadosModel" usando o serviço "dadosService"
        DadosModel dadosSalvos = dadosService.criar(dadosModel);
        // retorna uma resposta HTTP 201 CREATED com o objeto salvo no corpo da resposta
        return ResponseEntity.status(HttpStatus.CREATED).body(dadosSalvos);
    }

    @Tag(name = "Apagar")
    @Operation(summary = "Efetua a deleção a partir do Id informado.")
    @DeleteMapping("/apague/{id}")
    //Busca o objeto DadosModel com o id fornecido
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        //Optional = um valor pode ou nao estar presente
        Optional<DadosModel> dadosOptional = dadosService.buscarPorId(id);
        // verifica se o objeto foi encontrado, e deleta
        if (dadosOptional.isPresent()) {
            //se for encontrado
            dadosService.deletar(id);
            // Retorna uma resposta com status 204 No Content, que indica que a operação foi bem-sucedida
            // mas não há corpo para ser retornado
            return ResponseEntity.noContent().build();
        }
        // Se o objeto não foi encontrado, cria uma instancia com uma mensagem
        ErrorResponse error = new ErrorResponse("Dados não encontrados para o id: " + id);
        // Retorna uma resposta com status 404 Not Found
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @Tag(name = "Atualizar")
    @Operation(summary = "Atualização de dados já existentes para novos dados.")
    @PutMapping("atualizar/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody DadosModel dados) {
        // Busca o objeto DadosModel com o id fornecido
        Optional<DadosModel> dadosOptional = dadosService.buscarPorId(id);
        // Verifica se o objeto foi encontrado
        if (dadosOptional.isPresent()) {
            // Se o objeto foi encontrado, atualiza
            DadosModel dadosAtualizados = dadosService.atualizar(id, dados);
            return ResponseEntity.ok(dadosAtualizados);
        }
        //se nao foi encontrado, retorna a mensagem
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Dados não encontrados para o ID informado."));
    }

    @Tag(name = "Consultar taxa de juros mensal por Id")
    @Operation(summary = "Consultar taxa de juros mensal por Id.")
    @GetMapping("/taxaMensal/{id}")
    public ResponseEntity<?> buscarTaxaPorId(@PathVariable Long id) {
        // Busca o objeto DadosModel com o id fornecido
        Optional<DadosModel> dadosOptional = dadosService.buscarPorId(id);
        // Verifica se o objeto foi encontrado
        if (dadosOptional.isPresent()) {
            //se o objeto for enconttado, ele retorna a taxa de juros
            Double taxaJuros = dadosOptional.get().getTaxaJurosAoMes();
            return ResponseEntity.ok(taxaJuros);
        } else {
            //Senao, cria uma instancia e retorna a mensagem de erro no corpo da mensagem
            ErrorResponse error = new ErrorResponse("Dados não encontrados para o id: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @Tag(name = "Listar taxa de juros com paginação")
    @Operation(summary = "Listar taxa de juros com paginação.")
    @GetMapping("/paginado")
    public ResponseEntity<Page<DadosModel>> listarComPaginacao(
            @RequestParam(defaultValue = "0") Integer pagina, // parâmetro opcional para a página
            @RequestParam(defaultValue = "10") Integer tamanho) { // parâmetro opcional para o tamanho da página
        try {
            // chama o serviço dadosService para buscar uma página de objetos DadosModel com a paginação fornecida
            Page<DadosModel> dadosPaginados = dadosService.listarComPaginacao(pagina, tamanho);
            // retorna a página encontrada com o status HTTP 200 OK
            return ResponseEntity.ok(dadosPaginados);
            // se os parâmetros de paginação forem inválidos, lança uma exceção
        } catch (IllegalArgumentException e) {
            // retorna um status HTTP 400 Bad Request
            return ResponseEntity.badRequest().build();
        }
    }

    @Tag(name = "Listar taxa de juros por ano e mês")
    @Operation(summary = "Listar taxa de juros por ano e mês.")
    @GetMapping("/ano-mes/{anoMes}")
    public ResponseEntity<?> listarPorAnoMes(@PathVariable String anoMes) {
        // verificação do formato da string "anoMes"
        if (!anoMes.matches("^\\d{4}-\\d{2}$")) {
            return ResponseEntity.badRequest()
                    .body("Formato de ano-mês inválido. O formato correto é: YYYY-MM, tente novamente de forma semelhante ao exemplo:2023-01.");
        }
        // busca os dados correspondentes ao ano e mês informados usando o serviço "dadosService"
        List<DadosModel> dados = dadosService.listarPorAnoMes(anoMes);
        // retorna uma resposta com status HTTP 200 OK
        return ResponseEntity.ok(dados);
    }

    @Tag(name = "Listar dados por instituição")
    @Operation(summary = "Listar dados por instituição informada.")
    @GetMapping("/instituicao")
    //chama o metodo listarPorInstuicao do service, passando o nome da instituicao como parametro
    public ResponseEntity listarPorInstituicao(@RequestParam("InstituicaoFinanceira") String InstituicaoFinanceira) {
        try {
            List<DadosModel> dadosFiltrados = dadosService.listarPorInstituicaoFinanceira(InstituicaoFinanceira);
            if (dadosFiltrados.isEmpty()) {
                ErrorResponse error = new ErrorResponse("Nenhuma instituição financeira encontrada com o nome informado.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
            return ResponseEntity.ok(dadosFiltrados);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

