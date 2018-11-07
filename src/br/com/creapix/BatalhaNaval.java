package br.com.creapix;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class BatalhaNaval {

	static String nomeJogador1, nomeJogador2;

	static int tamanhoX, tamanhoY, quantidadeDeNavios, limiteMaximoDeNavios;
	static int tabuleiroJogador1[][], tabuleiroJogador2[][];
	static Scanner input = new Scanner(System.in);

	public static void obterTamanhoDosTabuleiros() {
		for (;;) {
			boolean tudoOk = false;
			try {
				System.out.println("Digite a altura do tabuleiro: ");
				tamanhoX = input.nextInt();
				System.out.println("Digite a largura do tabuleiro: ");
				tamanhoY = input.nextInt();
				tudoOk = true;
			} catch (InputMismatchException erro) {
				System.out.println("Digite um valor numérico");
			}
			if (tudoOk) {
				break;
			}
		}
	}

	public static void obterNomesDosJogadores() {
		System.out.println("Digite o nome do Jogador 1");
		nomeJogador1 = input.nextLine();
		// System.out.println("Digite o nome do Jogador 2");
		nomeJogador2 = "Computador";
	}

	public static void calcularQuantidadeMaximaDeNaviosNoJogo() {
		limiteMaximoDeNavios = tamanhoX * tamanhoY / 3;
	}

	public static void iniciandoTamanhoDosTabuleiros() {
		tabuleiroJogador1 = retornarNovoTabuleiroVazio();
		tabuleiroJogador2 = retornarNovoTabuleiroVazio();
	}

	public static int[][] retornarNovoTabuleiroVazio() {
		return new int[tamanhoX][tamanhoY];
	}

	public static void obterQuantidadeDeNavios() {
		System.out.println("Digite a quantidade de Navios: ");
		System.out.println("Max: " + limiteMaximoDeNavios + " navios");
		quantidadeDeNavios = input.nextInt();
		if (quantidadeDeNavios < 1 || quantidadeDeNavios > limiteMaximoDeNavios) {
			quantidadeDeNavios = limiteMaximoDeNavios;
		}
	}

	public static int[][] distribuirNaviosEmUmNovoTabuleiro() {
		int novoTabuleiro[][] = retornarNovoTabuleiroVazio();
		int quantidadeRestanteDeNavios = quantidadeDeNavios;
		int x = 0, y = 0;
		Random numeroAleatorio = new Random();
		do {
			x = 0;
			y = 0;
			for (int[] linha : novoTabuleiro) {
				for (int coluna : linha) {
					if (numeroAleatorio.nextInt(100) <= 10) {
						if (coluna == 0) {
							novoTabuleiro[x][y] = 1;
							quantidadeRestanteDeNavios--;
							break;
						}
						if (quantidadeRestanteDeNavios <= 0) {
							break;
						}
					}
					y++;
				}
				y = 0;
				x++;
				if (quantidadeRestanteDeNavios <= 0) {
					break;
				}
			}
		} while (quantidadeRestanteDeNavios > 0);
		return novoTabuleiro;
	}

	public static void inserirNaviosNosTabuleiros() {
		tabuleiroJogador1 = distribuirNaviosEmUmNovoTabuleiro();
		tabuleiroJogador2 = distribuirNaviosEmUmNovoTabuleiro();
	}

	public static void exibirTabuleiro(String nomeDoJogador, int[][] tabuleiro, boolean ai) {
		System.out.println("");
		System.out.println("");
		System.out.println(" ##### Tabuleiro do " + nomeDoJogador + " ##### ");
		System.out.println("");
		exibirLetrasDasColunasDoTabuleiro();
		String linhaDoTabuleiro = "";
		boolean seuTabuleiro = ai;
		int numeroDaLinha = 1;
		for (int[] linha : tabuleiroJogador1) {
			if (numeroDaLinha < 10) {
				linhaDoTabuleiro = (numeroDaLinha++) + "  |";
			} else {
				linhaDoTabuleiro = (numeroDaLinha++) + " |";
			}
			for (int coluna : linha) {
				switch (coluna) {
				case 0: // vazio ou sem ação
					linhaDoTabuleiro += "   |";
					break;
				case 1: // Navio
					if (seuTabuleiro) {
						linhaDoTabuleiro += " N |";
						break;
					} else {
						linhaDoTabuleiro += "   |";
						break;
					}
				case 2: // Erro
					linhaDoTabuleiro += " X |";
					break;
				case 3: // Acertou
					linhaDoTabuleiro += " D |";
					break;
				}
			}
			System.out.println(linhaDoTabuleiro);
		}
	}

	public static void exibirLetrasDasColunasDoTabuleiro() {
		char letraDaColuna = 65;
		String letrasDoTabuleiro = "     ";
		for (int i = 0; i < tamanhoY; i++) {
			letrasDoTabuleiro += (letraDaColuna++) + "   ";
		}
		System.out.println(letrasDoTabuleiro);
	}

	public static void exibirTabuleiroDosJogadores() {
		exibirTabuleiro(nomeJogador1, tabuleiroJogador1, true);
		exibirTabuleiro(nomeJogador2, tabuleiroJogador2, false);
	}

	public static void main(String[] args) {
		/*
		 * Tabuleiros 1 - Jogador 2 - Computador / Outro Jogador
		 */
		obterNomesDosJogadores();
		obterTamanhoDosTabuleiros();
		calcularQuantidadeMaximaDeNaviosNoJogo();
		iniciandoTamanhoDosTabuleiros();
		obterQuantidadeDeNavios();
		inserirNaviosNosTabuleiros();
		exibirTabuleiroDosJogadores();
		
		acaoDoJogador(); 
		
		

		input.close();
	}

	public static void acaoDoJogador() {
		System.out.println("Digite a posição do seu tiro ('LetraNumeros' ex. A1, E23):  ");
		String tiroDoJogador = input.next();
		int quantidadeDeNumeros = (tamanhoY >= 10) ? 2 : 1;
		String expressaoDeVerificacao = "^[A-Za-z] {1} [0-9] {"+ quantidadeDeNumeros + "}$";
		
		
		//Verificações
		if (tiroDoJogador.matches(expressaoDeVerificacao)) {
			String tiro = tiroDoJogador.toLowerCase();
			int posicaoX = tiro.charAt(0) - 97;
			
			int posicaoY = Integer.parseInt(tiro.substring(1)) - 1;
			
			if (verificarPosicaoInseridaPeloJogador(posicaoX, posicaoY)) {
				System.out.println("OK - Disparando");
			}
			
		} else {
			System.out.println("Coordenadas inválidas");
		}
	}

	public static boolean verificarPosicaoInseridaPeloJogador(int posicaoX, int posicaoY) {
		boolean retorno = true;
		if (posicaoX > tamanhoX - 1) {
			retorno = false;
			System.out.println("A posição das letras não pode ser maior que "+ (char) tamanhoX + 64);
		}
		if (posicaoY > tamanhoY) {
			retorno = false;
			System.out.println("A posição numérica mão pode ser maior que " + tamanhoY);
		}
		return retorno;
	}

}