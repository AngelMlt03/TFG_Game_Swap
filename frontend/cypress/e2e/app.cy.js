describe('Smoke Test - GameSwap', () => {
  
  it('Debe cargar la página de inicio y responder el backend', () => {
    // 1. Visitamos la URL del frontend (la que levanta el YAML)
    cy.visit('http://localhost:4200');

    // 2. Comprobamos que el título o algún texto clave existe
    // Cambia 'Game Swap' por el texto que aparezca en tu Header/Home
    //cy.contains('Game Swap').should('be.visible');

    // 3. Opcional: Probar una llamada real al backend
    // Si tienes una ruta como /api/usuarios o /api/juegos, podemos ver si responde
    // Esto confirma que Flyway y H2 han funcionado correctamente
    //cy.request('http://localhost:8080/api/juegos').then((response) => {
      //expect(response.status).to.eq(200);
      // Si tu V1__init.sql mete datos, podrías incluso mirar si hay contenido
      // expect(response.body).to.have.length.greaterThan(0);
    //});
  });

  //it('Debe mostrar el formulario de login', () => {
    //cy.visit('http://localhost:4200/login'); // Asumiendo que esta es tu ruta
    //cy.get('input[name="username"]').should('exist');
    //cy.get('input[name="password"]').should('exist');
    //cy.get('button[type="submit"]').should('be.visible');
  //});
});