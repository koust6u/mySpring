package com.fm.server.model;

import com.fm.server.model.PlayerQuery;
import com.fm.server.resource.ResourceFile;
import com.fm.unit.Player;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PlayerRepository {
    private static PlayerRepository a = new PlayerRepository();
    private boolean b = false;
    private List c;

    private PlayerRepository() {
    }

    public static PlayerRepository getInstance() {
        return a;
    }

    public List load() {
        if (!this.isLoaded()) {
            ArrayList var1 = new ArrayList();
            Scanner var2;
            if ((var2 = a(ResourceFile.getResourceFileName())).hasNext()) {
                var2.nextLine();
            }

            while(var2.hasNext()) {
                String[] var3 = var2.nextLine().split(",");
                Player var4 = new Player(ResourceFile.getId(var3), ResourceFile.getName(var3), ResourceFile.getHeight(var3), ResourceFile.getNationality(var3), ResourceFile.getClub(var3), ResourceFile.getOverall(var3), ResourceFile.getPosition(var3));
                var1.add(var4);
            }

            this.c = var1;
            this.b = true;
            var2.close();
        }

        return this.c;
    }

    private static Scanner a(String var0) {
        Scanner var1 = null;

        try {
            var1 = new Scanner(new BufferedInputStream(new FileInputStream(var0)));
        } catch (FileNotFoundException var2) {
            var2.printStackTrace();
        }

        return var1;
    }

    public boolean isLoaded() {
        return this.b;
    }

    public List query(List var1) {
        return (List)(0 < var1.size() && 0 < this.c.size() ? PlayerQuery.query(this.c, var1) : new ArrayList());
    }
}
