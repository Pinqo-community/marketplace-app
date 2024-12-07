export interface PrimaryButtonProps {
  children: React.ReactNode;
  light?: boolean;
}

export interface MenuButtonProps {
  isOpened: boolean;
  toggleMenu: () => void;
}
