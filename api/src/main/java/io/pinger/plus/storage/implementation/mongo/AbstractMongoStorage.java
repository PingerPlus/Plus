package io.pinger.plus.storage.implementation.mongo;

import com.google.common.base.Strings;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import io.pinger.plus.storage.credentials.StorageCredentials;
import io.pinger.plus.storage.implementation.StorageImplementation;
import org.bson.UuidRepresentation;

public abstract class AbstractMongoStorage implements StorageImplementation {
    private final StorageCredentials configuration;
    private final String connectionUri;

    private MongoClient mongoClient;

    protected MongoDatabase database;

    public AbstractMongoStorage(StorageCredentials configuration, String connectionUri) {
        this.configuration = configuration;
        this.connectionUri = connectionUri;
    }

    @Override
    public void init() {
        final MongoClientOptions.Builder options = MongoClientOptions
            .builder()
            .uuidRepresentation(UuidRepresentation.JAVA_LEGACY);

        if (!Strings.isNullOrEmpty(this.connectionUri)) {
            this.mongoClient = new MongoClient(new MongoClientURI(this.connectionUri, options));
        } else {
            MongoCredential credential = null;
            if (!Strings.isNullOrEmpty(this.configuration.getUsername())) {
                credential = MongoCredential.createCredential(
                    this.configuration.getUsername(),
                    this.configuration.getDatabase(),
                    Strings.isNullOrEmpty(this.configuration.getPassword()) ? null : this.configuration.getPassword().toCharArray()
                );
            }

            final String[] addressSplit = this.configuration.getAddress().split(":");
            final String host = addressSplit[0];
            final int port = addressSplit.length > 1 ? Integer.parseInt(addressSplit[1]) : 27017;
            final ServerAddress address = new ServerAddress(host, port);

            if (credential == null) {
                this.mongoClient = new MongoClient(address, options.build());
            } else {
                this.mongoClient = new MongoClient(address, credential, options.build());
            }
        }

        this.database = this.mongoClient.getDatabase(this.configuration.getDatabase());
    }

    @Override
    public void shutdown() {
        if (this.mongoClient != null) {
            this.mongoClient.close();
        }
    }
}
