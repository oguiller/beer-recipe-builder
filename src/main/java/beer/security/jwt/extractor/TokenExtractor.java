package beer.security.jwt.extractor;

public interface TokenExtractor {
    public String extract(String payload);
}