package io.trydent.olimpo.db;

import io.trydent.olimpo.sys.Lazy;
import io.trydent.olimpo.type.Type;
import io.trydent.olimpo.vertx.json.Json;
import io.vertx.core.Vertx;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLClient;

import static io.trydent.olimpo.sys.Lazy.lazy;

public interface DbmsClient extends Type.As<SQLClient> {
  static DbmsClient database(final Vertx vertx, final Json params) {
    return new SimpleDbmsClient(vertx, params);
  }
}

final class SimpleDbmsClient implements DbmsClient {
  private final Lazy<SQLClient> client;

  SimpleDbmsClient(final Vertx vertx, final Json params) {
    this(
      lazy(() -> JDBCClient.createNonShared(vertx, params.get()))
    );
  }
  private SimpleDbmsClient(Lazy<SQLClient> client) {
    this.client = client;
  }

  @Override
  public final SQLClient get() {
    return this.client.get();
  }
}
