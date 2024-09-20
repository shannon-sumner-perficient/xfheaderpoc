const header = {
  closeFlyout(target: HTMLElement) {
    (this.timer as number | NodeJS.Timeout) = setTimeout(() => {
      this.getTarget(target)?.classList.remove('open');
    }, 100);
  },
  getTarget(target: HTMLElement) {
    if (target.classList.contains('header-nav__list-item--with-flyout')) {
      return target;
    }
    return target.closest('.header-nav__list-item--with-flyout');
  },
  init(headerNav: HTMLDivElement) {
    const menuButton = document.querySelector('.header-nav__menu-button');
    const listItemsWithFlyout = document.querySelectorAll(
      '.header-nav__list-item--with-flyout',
    );
    this.initializelistItemsWithFlyout(
      listItemsWithFlyout as NodeListOf<HTMLDListElement>,
    );
    this.updateClassList(headerNav);
    if (menuButton) {
      menuButton.addEventListener('click', () => {
        if (headerNav.classList.contains('header-nav--displayed')) {
          headerNav.classList.add('header-nav--collapsed');
          headerNav.classList.remove('header-nav--displayed');
          setTimeout(() => {
            headerNav.classList.remove('header-nav--visible');
          }, 250);
        } else {
          headerNav.classList.remove('header-nav--collapsed');
          headerNav.classList.add('header-nav--displayed');
          headerNav.classList.add('header-nav--visible');
        }
      });
    }
  },
  initializelistItemsWithFlyout(
    listItemsWithFlyout: NodeListOf<HTMLDListElement>,
  ) {
    listItemsWithFlyout.forEach((listItemWithFlyout) => {
      listItemWithFlyout.addEventListener('mouseover', (event: MouseEvent) => {
        if (!this.isMobile()) {
          this.openFlyout(listItemsWithFlyout, event.target as HTMLElement);
        }
      });
      listItemWithFlyout.addEventListener('mouseout', (event: MouseEvent) => {
        if (!this.isMobile()) {
          this.closeFlyout(event.target as HTMLElement);
        }
      });
      listItemWithFlyout.addEventListener('click', (event: MouseEvent) => {
        const target = this.getTarget(event.target as HTMLElement);
        if (target?.classList.contains('open')) {
          target?.classList.remove('open');
          target?.setAttribute('aria-expanded', 'false');
        } else {
          listItemsWithFlyout.forEach((listItemWithFlyout) => {
            listItemWithFlyout.classList.remove('open');
          });
          target?.classList.add('open');
          target?.setAttribute('aria-expanded', 'true');
        }
      });
    });
    document.onkeydown = (event: KeyboardEvent) => {
      if (event.key === 'Escape') {
        listItemsWithFlyout.forEach((listItemWithFlyout) => {
          listItemWithFlyout.classList.remove('open');
        });
      }
    };
  },
  isMobile() {
    return window.matchMedia('(max-width: 970px)').matches;
  },
  openFlyout(
    listItemsWithFlyout: NodeListOf<HTMLDListElement>,
    target: HTMLElement,
  ) {
    listItemsWithFlyout.forEach((listItemWithFlyout) => {
      listItemWithFlyout.classList.remove('open');
    });
    this.getTarget(target)?.classList.add('open');
    clearTimeout(this.timer);
  },
  timer: 0,
  updateClassList(headerNav: HTMLDivElement) {
    if (this.isMobile()) {
      headerNav.classList.add('header-nav--collapsed');
    } else {
      headerNav.classList.remove(
        'header-nav--collapsed',
        'header-nav--displayed',
        'header-nav--visible',
      );
    }
  },
};

document.addEventListener('DOMContentLoaded', () => {
  const headerNav = document.querySelector('.header-nav');
  if (headerNav) {
    header.init(headerNav as HTMLDivElement);
    window.addEventListener('resize', () => {
      header.updateClassList(headerNav as HTMLDivElement);
    });
  }
});
