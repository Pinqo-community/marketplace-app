import Header from "../components/Header/Header";

const MainLayout: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  return (
    <div className="main-layout">
      <Header />
      <main>{children}</main>
    </div>
  );
};

export default MainLayout;
