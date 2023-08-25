package io.github.Leonardo0013YT.UltraMinions.utils;

import io.github.Leonardo0013YT.UltraMinions.Main;
import io.github.Leonardo0013YT.UltraMinions.calls.CallBackAPI;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class DependUtils {

    private Main plugin;

    public DependUtils(Main plugin) {
        this.plugin = plugin;
    }

    public void loadDepends() {
        File libs = new File(plugin.getDataFolder(), "libs");
        if (!libs.exists()) {
            libs.mkdirs();
        }
        try {
            File hikari = new File(plugin.getDataFolder(), "libs/HikariCP-3.4.2.jar");
            if (!hikari.exists()) {
                this.download(new URL("https://repo1.maven.org/maven2/com/zaxxer/HikariCP/3.4.2/HikariCP-3.4.2.jar"), "HikariCP-3.4.2.jar");
            }
            File commons = new File(plugin.getDataFolder(), "libs/commons-pool2-2.8.0.jar");
            if (!commons.exists()) {
                this.download(new URL("https://repo1.maven.org/maven2/org/apache/commons/commons-pool2/2.8.0/commons-pool2-2.8.0.jar"), "commons-pool2-2.8.0.jar");
            }
            try {
                Class.forName("org/slf4j/LoggerFactory");
            } catch (ClassNotFoundException e) {
                File slf4j = new File(plugin.getDataFolder(), "libs/slf4j-api-1.7.25.jar");
                if (!slf4j.exists()) {
                    this.download(new URL("https://repo1.maven.org/maven2/org/slf4j/slf4j-api/1.7.25/slf4j-api-1.7.25.jar"), "slf4j-api-1.7.25.jar");
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void downloadDependency(URL url, File fileName, CallBackAPI<Double> callback) {
        BufferedInputStream in = null;
        FileOutputStream out = null;

        try {
            URLConnection conn = url.openConnection();
            int size = conn.getContentLength();

            in = new BufferedInputStream(url.openStream());
            out = new FileOutputStream(fileName);
            byte[] data = new byte[1024];
            int count;
            double sumCount = 0.0;

            while ((count = in.read(data, 0, 1024)) != -1) {
                out.write(data, 0, count);

                sumCount += count;
                if (size > 0) {
                    double porcentage = (sumCount / size * 100.0);
                    callback.done(porcentage);
                }
            }

        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            if (out != null)
                try {
                    out.close();
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
        }
    }

    private void download(URL url, String name) {
        File libraries = new File(plugin.getDataFolder(), "libs");
        if (!libraries.exists()) {
            libraries.mkdir();
        }

        File fileName = new File(libraries, name + ".jar");
        if (!fileName.exists()) {
            try {
                fileName.createNewFile();
                downloadDependency(url, fileName, value -> {
                    if (value >= 100) {
                        plugin.sendLogMessage("Downloading §b" + name + " §e" + String.format("%.1f", value).replaceAll(",", ".") + "%");
                        loadJarFile(fileName);
                        plugin.sendLogMessage("Download of §b" + name + " §ehas been completed.");
                    }
                });
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            loadJarFile(fileName);
        }

    }

    private void loadJarFile(File jar) {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            Class<?> getClass = classLoader.getClass();
            Method method = getClass.getSuperclass().getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            method.invoke(classLoader, jar.toURI().toURL());
        } catch (NoSuchMethodException | MalformedURLException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}