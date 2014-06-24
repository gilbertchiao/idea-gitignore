package mobi.hsz.idea.gitignore;

import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;
import mobi.hsz.idea.gitignore.lang.GitignoreLanguage;
import org.jetbrains.annotations.NotNull;

public class GitignoreFileTypeFactory extends FileTypeFactory {
    @Override
    public void createFileTypes(@NotNull FileTypeConsumer consumer) {
        consumer.consume(GitignoreFileType.INSTANCE, GitignoreLanguage.EXTENSION);
    }
}
