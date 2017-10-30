package act.plugin.finder;

import act.app.AppByteCodeScanner;
import act.app.AppByteCodeScannerBase;
import act.asm.AnnotationVisitor;
import act.asm.Type;
import act.util.ByteCodeVisitor;
import org.osgl.$;
import org.osgl.exception.NotAppliedException;
import org.osgl.logging.LogManager;
import org.osgl.logging.Logger;

/**
 * Function: 扫描所有 Scanner 结尾的类,如果加了 @Scanner,则注册到系统扫描器列表里
 *
 * @Autor: leeton
 * @Date : 10/28/17
 */
public class ScannerFinder extends AppByteCodeScannerBase {
    protected static Logger logger = LogManager.get(ScannerFinder.class);
    private String className;

    @Override
    public ByteCodeVisitor byteCodeVisitor() {
        return new AopVisiter();
    }

    @Override
    public void scanFinished(String className) {

    }

    @Override
    protected boolean shouldScan(String className) {
        this.className = className;
        return className.endsWith("Scanner");
    }


    private class AopVisiter extends ByteCodeVisitor {

        private boolean found;

        @Override
        public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
            AnnotationVisitor av = super.visitAnnotation(desc, visible);
            if (isScanner(desc)) {
                found = true;
            }
            return null;
        }

        private boolean isScanner(String desc) {
            return Type.getType(Scanner.class).getDescriptor().equals(desc);
        }


        @Override
        public void visitEnd() throws NotAppliedException, $.Break {
            super.visitEnd();
            if (found) {
                try {
                    Class<AppByteCodeScanner> autoConfigClass = $.classForName(className, app().classLoader());
                    app().scannerManager().register(autoConfigClass.newInstance());
                    logger.debug(" FIND SCANNER AND REG = [" + autoConfigClass + "]");
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

    }


}
