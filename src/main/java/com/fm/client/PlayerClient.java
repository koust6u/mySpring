package com.fm.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fm.search.SearchCondition;
import com.fm.search.SearchField;
import com.fm.unit.Player;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class PlayerClient {

    private static final String PORT = "9000";

    private static final String HOST = "localhost";

    public PlayerClient() {
    }

    public static void main(String... args){
        PlayerClient client = new PlayerClient();
        Scanner scanner = new Scanner(System.in);

        while(scanner.hasNextLine()){
            String line = scanner.nextLine().trim();
            if (!line.equals("")){
                List<SearchCondition> conditionList = toSearchConditionList(line);
                client.requestPlayer(conditionList);
            }
        }

        scanner.close();
    }


    private static List<SearchCondition> toSearchConditionList(String str){
        List<SearchCondition> list = new ArrayList<>();
        Scanner scanner = new Scanner(str);

        while (scanner.hasNext()){
            String field = scanner.next();
            if (scanner.hasNext()){
                String condition = scanner.next();
                list.add(new SearchCondition(SearchField.fromString(field), condition));
            }
        }
        return list;
    }

    private static HttpRequest toHttpRequest(String handler){
        return HttpRequest.newBuilder().uri(URI.create("http://" + HOST + ":" + PORT + handler))
                .header("Content-Type", "application/json").build();
    }


    public void requestPlayer(List<SearchCondition> conditions){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = toHttpRequest(toPlayerURL(conditions));
        String result =  (String) client.sendAsync(request, BodyHandlers.ofString()).thenApply(HttpResponse::body).join();
        ObjectMapper mapper = new ObjectMapper();

        try{
            CollectionType playerList = mapper.getTypeFactory().constructCollectionType(List.class, Player.class);
            List<Player> players = (List)mapper.readValue(result, playerList);
            System.out.println(players);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private static String toPlayerURL(List<SearchCondition> conditions){
        String url = "/players?";

        SearchCondition condition;
        for (Iterator i  = conditions.iterator(); i.hasNext(); url = url + condition.getField().toString() + "="
        + condition.getCondition() + "&"){
            condition = (SearchCondition) i.next();
        }

        return url;
    }
}
