package mobi.hsz.idea.gitignore.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReference;
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReferenceSet;
import com.intellij.util.containers.ContainerUtil;
import mobi.hsz.idea.gitignore.psi.GitignoreEntry;
import mobi.hsz.idea.gitignore.util.Utils;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;

public class GitReferenceSet extends FileReferenceSet {
    public GitReferenceSet(@NotNull GitignoreEntry element) {
        super(element);
    }

    @Override
    public FileReference createFileReference(TextRange range, int index, String text) {
        return new GitReference(this, range, index, text);
    }

    private class GitReference extends FileReference {
        public GitReference(@NotNull FileReferenceSet fileReferenceSet, TextRange range, int index, String text) {
            super(fileReferenceSet, range, index, text);
        }

        @NotNull
        @Override
        protected ResolveResult[] innerResolve(boolean caseSensitive) {
            LinkedList<ResolveResult> result = ContainerUtil.newLinkedList();
            PsiManager psiManager = PsiManager.getInstance(getElement().getProject());
            for (PsiFileSystemItem context : getContexts()) {
                VirtualFile contextVirtualFile = context.getVirtualFile();
                if (contextVirtualFile != null) {
                    String regexFromGlob = Utils.createRegexFromGlob(getCanonicalText());
                    for (VirtualFile file : contextVirtualFile.getChildren()) {
                        if (file.getName().matches(regexFromGlob)) {
                            PsiDirectory psiDirectory = psiManager.findDirectory(file);
                            if (psiDirectory != null) {
                                result.add(new PsiElementResolveResult(psiDirectory));
                            }
                            PsiFile psiFile = psiManager.findFile(file);
                            if (psiFile != null) {
                                result.add(new PsiElementResolveResult(psiFile));
                            }
                        }
                    }
                }
            }
            return result.isEmpty()
                    ? super.innerResolve(caseSensitive)
                    : result.toArray(new ResolveResult[result.size()]);
        }
    }
}
