package com.xinghai.fileServer.service.nettyInit;

import io.netty.handler.codec.http.HttpMethod;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
/**
 * 请求映射类
 */
public class RequestMappingInfo {
    private Set<String> patterns;
    private Set<HttpMethod> methods;

    public RequestMappingInfo() {
    }
    public RequestMappingInfo(String[] patterns, HttpMethod method) {
        this.patterns = new HashSet<>(Arrays.asList(patterns));
        this.methods = new HashSet<HttpMethod>(){{add(method);}};
    }
    public RequestMappingInfo(String pattern, HttpMethod method) {
        this.patterns = new HashSet<>(Collections.singletonList(pattern));
        this.methods = new HashSet<>(Collections.singletonList(method));
    }
    public Set<String> getPatterns() {
        return patterns;
    }

    public void setPatterns(Set<String> patterns) {
        this.patterns = patterns;
    }

    public Set<HttpMethod> getMethods() {
        return methods;
    }

    public void setMethods(Set<HttpMethod> methods) {
        this.methods = methods;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RequestMappingInfo)) return false;

        RequestMappingInfo that = (RequestMappingInfo) o;

        if (patterns != null ? !patterns.equals(that.patterns) : that.patterns != null) return false;
        return methods != null ? methods.equals(that.methods) : that.methods == null;

    }

    @Override
    public int hashCode() {
        int result = patterns != null ? patterns.hashCode() : 0;
        result = 31 * result + (methods != null ? methods.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "{" +
                "patterns=" + patterns +
                ", methods=" + methods +
                '}';
    }
}
