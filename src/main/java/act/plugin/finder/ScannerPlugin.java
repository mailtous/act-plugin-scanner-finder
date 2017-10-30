package act.plugin.finder;

import act.app.App;
import act.app.AppByteCodeScanner;
import act.app.AppSourceCodeScanner;
import act.util.AppCodeScannerPluginBase;

/**
 * Function: 扫描器 plugin
 *
 * @Autor: leeton
 * @Date : 10/28/17
 */
public class ScannerPlugin extends AppCodeScannerPluginBase {

    @Override
    public AppSourceCodeScanner createAppSourceCodeScanner(App app) {
        return null;
    }

    @Override
    public AppByteCodeScanner createAppByteCodeScanner(App app) {
        return new ScannerFinder();
    }

    @Override
    public boolean load() {
        return true;
    }
}
