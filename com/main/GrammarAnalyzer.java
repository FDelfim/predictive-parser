package com.main;

import java.util.*;

import java.util.*;

public class GrammarAnalyzer {
    
    private Map<String, List<List<String>>> productions;
    
    public GrammarAnalyzer(Map<String, List<List<String>>> productions) {
        this.productions = productions;
    }
    
    public Set<String> findFirst(String symbol) {
        Set<String> firstSet = new HashSet<>();
        
        // Se o símbolo é terminal, adiciona ele mesmo ao conjunto First
        if (!productions.containsKey(symbol)) {
            firstSet.add(symbol);
            return firstSet;
        }
        
        // Para cada produção do símbolo
        for (List<String> production : productions.get(symbol)) {
            int i = 0;
            while (i < production.size()) {
                String currentSymbol = production.get(i);
                
                // Se o símbolo atual é não-terminal, adiciona o conjunto First dele ao conjunto First do símbolo original
                if (productions.containsKey(currentSymbol)) {
                    Set<String> currentFirst = findFirst(currentSymbol);
                    firstSet.addAll(currentFirst);
                    
                    // Se o conjunto First do símbolo atual não contém vazio, sai do loop
                    if (!currentFirst.contains(" ")) {
                        break;
                    }
                } else {
                    // Se o símbolo atual é terminal, adiciona ele mesmo ao conjunto First do símbolo original e sai do loop
                    firstSet.add(currentSymbol);
                    break;
                }
                
                i++;
            }
            
            // Se todos os símbolos da produção têm conjunto First contendo vazio, adiciona vazio ao conjunto First do símbolo original
            if (i == production.size()) {
                firstSet.add(" ");
            }
        }
        
        return firstSet;
    }
    
    public Set<String> findFollow(String symbol) {
        Set<String> followSet = new HashSet<>();
        
        // Adiciona $ ao conjunto Follow do símbolo inicial
        if (symbol.equals("Goal")) {
            followSet.add("$");
        }
        
        // Para cada símbolo da gramática
        for (String s : productions.keySet()) {
            // Para cada produção do símbolo
            for (List<String> production : productions.get(s)) {
                // Encontra a posição do símbolo em questão na produção
                int i = production.indexOf(symbol);
                if (i == -1) {
                    continue;
                }
                
                // Se o símbolo está no final da produção, adiciona o conjunto Follow do símbolo original ao conjunto Follow do símbolo em questão
                if (i == production.size() - 1) {
                    if (!s.equals(symbol)) {
                        followSet.addAll(findFollow(s));
                    }
                } else {
                    String nextSymbol = production.get(i+1);
                    
                    // Se o próximo símbolo é não-terminal, adiciona o conjunto First dele ao conjunto Follow do símbolo em questão, exceto se contiver vazio, nesse caso adiciona o conjunto Follow do símbolo original
                    if (productions.containsKey(nextSymbol)) {
                        Set<String> nextFirst = findFirst(nextSymbol);
                        if (nextFirst.contains(" ")) {
                            nextFirst.remove(" ");
                            followSet.addAll(nextFirst);
                            if (!s.equals(symbol)) {
                                followSet.addAll(findFollow(s));
                            }
                        } else {
                            followSet.addAll(nextFirst);
                        }
                    } else {
                        // Se o próximo símbolo é terminal, adiciona ele mesmo ao conjunto Follow do símbolo em questão
                        followSet.add(nextSymbol);
                    }
                }
            }
        }
        
        return followSet;
    }
    
    public static void main(String[] args) {
        Map<String, List<List<String>>> productions = new HashMap<>();
        
        productions.put("Goal", Arrays.asList(
        Arrays.asList("Expr")
        ));
        
        productions.put("Expr", Arrays.asList(
        Arrays.asList("Term", "Expr'")    
        ));
        
        productions.put("Expr'", Arrays.asList(
        Arrays.asList("+", "Term", "Expr'"),
        Arrays.asList("-", "Term", "Expr'"),
        Arrays.asList()
        ));
        
        productions.put("Term", Arrays.asList(
        Arrays.asList("Factor", "Term'")
        ));
        
        productions.put("Term'", Arrays.asList(
        Arrays.asList("*", "Factor", "Term'"),
        Arrays.asList("/", "Factor", "Term'"),
        Arrays.asList()
        ));
        
        productions.put("Factor", Arrays.asList(
        Arrays.asList("(", "Expr", ")"),
        Arrays.asList("num"),
        Arrays.asList("nome")
        ));
        
        GrammarAnalyzer analyzer = new GrammarAnalyzer(productions);
        
        System.out.println("First(Goal) = " + analyzer.findFirst("Goal"));
        System.out.println("Follow(Goal) = " + analyzer.findFollow("Goal"));
        System.out.println("---------------------------------------------------");
        System.out.println("First(Expr) = " + analyzer.findFirst("Expr"));
        System.out.println("Follow(Expr) = " + analyzer.findFollow("Expr"));
        System.out.println("---------------------------------------------------");
        System.out.println("First(Term) = " + analyzer.findFirst("Term"));
        System.out.println("Follow(Term) = " + analyzer.findFollow("Term"));
        System.out.println("---------------------------------------------------");
        System.out.println("First(Factor) = " + analyzer.findFirst("Factor"));
        System.out.println("Follow(Factor) = " + analyzer.findFollow("Factor"));
        System.out.println("---------------------------------------------------");
        System.out.println("First(Expr') = " + analyzer.findFirst("Expr'"));
        System.out.println("Follow(Expr') = " + analyzer.findFollow("Expr'"));
        System.out.println("---------------------------------------------------");
        System.out.println("First(Term') = " + analyzer.findFirst("Term'"));
        System.out.println("Follow(Term') = " + analyzer.findFollow("Term'"));
    }
}