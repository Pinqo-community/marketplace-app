export interface Producer {
  id: number;
  profilePhoto: string;
  name: string;
  isOpen: boolean;
  location: { lat: number; lng: number };
}
