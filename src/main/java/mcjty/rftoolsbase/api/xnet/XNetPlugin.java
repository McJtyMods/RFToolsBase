package mcjty.rftoolsbase.api.xnet;

/** Fabric addons declare this implementation under the "xnet" entrypoint. */
@FunctionalInterface
public interface XNetPlugin {
    void register(IXNet api);
}
