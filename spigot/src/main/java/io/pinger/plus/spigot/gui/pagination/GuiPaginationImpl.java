package io.pinger.plus.spigot.gui.pagination;

import io.pinger.plus.spigot.gui.contents.GuiContents;
import io.pinger.plus.spigot.gui.item.GuiItem;
import io.pinger.plus.spigot.gui.template.button.ButtonState;
import io.pinger.plus.spigot.gui.template.button.GuiButtonTemplate;
import java.util.List;

public class GuiPaginationImpl<T> implements GuiPagination<T> {
    private final GuiContents contents;

    private int currentPage;
    private String identifier;
    private List<T> items;
    private PageItemProvider<T> provider;
    private List<Integer> slots;

    public GuiPaginationImpl(GuiContents contents) {
        this.contents = contents;
    }

    @Override
    public GuiPagination<T> addToInventory() {
        final GuiButtonTemplate template = this.contents.findButtonTemplate(this.identifier);
        if (template == null) {
            return this;
        }

        final List<T> items = this.getItemsInPage();
        for (int i = 0; i < this.items.size(); i++) {
            final int slot = this.slots.get(i);
            final T item = items.get(i);
            this.addItemToSlot(item, slot, template);
        }

        return this;
    }

    private void addItemToSlot(T item, int slot, GuiButtonTemplate template) {
        final ButtonState state = this.provider.provide(item, slot, template);
        if (state == null) {
            return;
        }

        this.contents.setItem(slot, GuiItem.of(state.getItem(), slot, state.getName(), template));
    }

    @Override
    public GuiPagination<T> setItems(String identifier, List<T> items, PageItemProvider<T> provider) {
        this.identifier = identifier;
        this.items = items;
        this.provider = provider;
        this.slots = this.contents.getItemSlots(identifier);
        return this;
    }

    @Override
    public GuiContents getContents() {
        return this.contents;
    }

    @Override
    public GuiPagination<T> setPage(int page) {
        this.currentPage = page;
        return this;
    }

    @Override
    public int getPage() {
        return this.currentPage;
    }

    @Override
    public int getPageCount() {
        if (this.items.isEmpty()) {
            return 0;
        }

        return (int) Math.ceil((double) this.items.size() / this.slots.size());
    }

    @Override
    public boolean isFirst() {
        return this.currentPage == 0;
    }

    @Override
    public boolean isLast() {
        return this.currentPage == this.getPageCount() - 1;
    }

    @Override
    public GuiPagination<T> next() {
        if (!this.isLast()) {
            this.currentPage++;
        }
        return this;
    }

    @Override
    public GuiPagination<T> previous() {
        if (!this.isFirst()) {
            this.currentPage--;
        }
        return this;
    }

    @Override
    public GuiPagination<T> first() {
        return this.setPage(0);
    }

    @Override
    public GuiPagination<T> last() {
        return this.setPage(this.getPageCount() - 1);
    }

    @Override
    public List<T> getItemsInPage() {
        final int itemsPerPage = this.slots.size();
        final int startIndex = this.currentPage * itemsPerPage;
        final int endIndex = Math.min(startIndex + itemsPerPage, this.items.size());
        return this.items.subList(startIndex, endIndex);
    }
}
