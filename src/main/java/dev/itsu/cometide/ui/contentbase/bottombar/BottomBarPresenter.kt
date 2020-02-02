package dev.itsu.cometide.ui.contentbase.bottombar

class BottomBarPresenter(val bottomBarImpl: BottomBarImpl) : IBottomBar.Presenter {
    override fun setLocation(line: Int, column: Int) {
        bottomBarImpl.setLocation(line + 1, column + 1)
    }

    override fun setStatus(text: String) {
        bottomBarImpl.setStatus(text)
    }
}