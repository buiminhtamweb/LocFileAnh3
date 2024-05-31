.class public Lcom/example/locfileanh3/HelloApplication;
.super Ljavafx/application/Application;
.source "HelloApplication.java"


# direct methods
.method public constructor <init>()V
    .registers 1

    .prologue
    .line 10
    invoke-direct {p0}, Ljavafx/application/Application;-><init>()V

    return-void
.end method

.method public static main([Ljava/lang/String;)V
    .registers 2
    .param p0, "args"    # [Ljava/lang/String;

    .prologue
    .line 21
    const/4 v0, 0x0

    new-array v0, v0, [Ljava/lang/String;

    invoke-static {v0}, Lcom/example/locfileanh3/HelloApplication;->launch([Ljava/lang/String;)V

    .line 22
    return-void
.end method


# virtual methods
.method public start(Ljavafx/stage/Stage;)V
    .registers 6
    .param p1, "stage"    # Ljavafx/stage/Stage;
    .annotation system Ldalvik/annotation/Throws;
        value = {
            Ljava/io/IOException;
        }
    .end annotation

    .prologue
    .line 13
    new-instance v0, Ljavafx/fxml/FXMLLoader;

    const-class v2, Lcom/example/locfileanh3/HelloApplication;

    const-string v3, "hello-view.fxml"

    invoke-virtual {v2, v3}, Ljava/lang/Class;->getResource(Ljava/lang/String;)Ljava/net/URL;

    move-result-object v2

    invoke-direct {v0, v2}, Ljavafx/fxml/FXMLLoader;-><init>(Ljava/net/URL;)V

    .line 14
    .local v0, "fxmlLoader":Ljavafx/fxml/FXMLLoader;
    new-instance v1, Ljavafx/scene/Scene;

    invoke-virtual {v0}, Ljavafx/fxml/FXMLLoader;->load()Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Ljavafx/scene/Parent;

    invoke-direct {v1, v2}, Ljavafx/scene/Scene;-><init>(Ljavafx/scene/Parent;)V

    .line 15
    .local v1, "scene":Ljavafx/scene/Scene;
    const-string v2, "Ph\u1ea7n m\u1ec1m l\u1ecdc \u1ea3nh!"

    invoke-virtual {p1, v2}, Ljavafx/stage/Stage;->setTitle(Ljava/lang/String;)V

    .line 16
    invoke-virtual {p1, v1}, Ljavafx/stage/Stage;->setScene(Ljavafx/scene/Scene;)V

    .line 17
    invoke-virtual {p1}, Ljavafx/stage/Stage;->show()V

    .line 18
    return-void
.end method
