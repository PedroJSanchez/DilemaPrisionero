/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agentes;

import dilemaPrisionero.OntologiaDilemaPrisionero;
import dilemaPrisionero.elementos.DilemaPrisionero;
import dilemaPrisionero.elementos.EntregarJugada;
import dilemaPrisionero.elementos.Jugada;
import dilemaPrisionero.elementos.JugadaEntregada;
import dilemaPrisionero.elementos.ProponerPartida;
import dilemaPrisionero.elementos.ResultadoJugada;
import jade.content.ContentManager;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.BeanOntologyException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ContractNetResponder;
import jade.proto.ProposeResponder;
import jade.proto.SubscriptionInitiator;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import juegos.elementos.DetalleInforme;
import juegos.elementos.GanadorPartida;
import juegos.elementos.Error;
import juegos.elementos.InformarPartida;
import juegos.elementos.Jugador;
import juegos.elementos.Partida;
import juegos.elementos.PartidaAceptada;
import static util.Constantes.BUSCAR_AGENTES;
import static util.Constantes.MAX_PARTIDAS;
import static util.Constantes.NOMBRE_JUGADOR_IMPAR;
import static util.Constantes.PRIMERO;
import static util.Constantes.RECOMPENSA;
import static util.Constantes.RETARDO_PRESENTACION;

/**
 *
 * @author pedroj
 */
public class AgenteLadronImpar extends Agent {
    
    private final ContentManager manager = (ContentManager) getContentManager();
	
    // El lenguaje utilizado por el agente para la comunicación es SL 
    private final Codec codec = new SLCodec();

    // La ontología que utilizará el agente
    private Ontology ontology;
    
    // Para enviar los mensajes a la consola
    private ArrayList<String> mensajesPendientes;
    private AID[] agentesConsola;
    
    // Variables
    private Jugador jugador;
    private int partidasActivas;
    private Map<AID, TareaInformarPartida> listaPolicias;
    private Map<String, DilemaPrisionero> partidas;
    private HashSet<String> traidores;
    
    @Override
    protected void setup() {
        
        //Incialización de variables
        mensajesPendientes = new ArrayList();
        definirMiJugador();
        partidasActivas = 0;
        listaPolicias = new HashMap();
        partidas = new HashMap();
        traidores = new HashSet();
        
        // Regisro de la Ontología
        try {
            ontology = OntologiaDilemaPrisionero.getInstance();
        } catch (BeanOntologyException ex) {
            Logger.getLogger(AgenteLadronImpar.class.getName()).log(Level.SEVERE, null, ex);
        }
        manager.registerLanguage(codec);
	manager.registerOntology(ontology);
        
        // Registro en las páginas Amarillas
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
	ServiceDescription sd = new ServiceDescription();
	sd.setType("Jugador");
	sd.setName(NOMBRE_JUGADOR_IMPAR);
	dfd.addServices(sd);
	try {
            DFService.register(this, dfd);
	}
	catch (FIPAException fe) {
            fe.printStackTrace();
	}
        
        //Añadir las tareas principales
        addBehaviour(new TareaBuscarConsola(this, BUSCAR_AGENTES ));
        addBehaviour(new TareaEnvioConsola(this, RETARDO_PRESENTACION));
        
        // Plantilla para la tarea ProponerPartida
//        MessageTemplate mtProponerPartida = 
//                MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_PROPOSE);
//        addBehaviour(new TareaProponerPartida(this, mtProponerPartida));
        
        mensajesPendientes.add("Se ha completado la inicialización del jugador: "
                + jugador.getNombre());
        
        System.out.println("Iniciado el agente: " + this.getLocalName());

        // Plantilla para la tarea JugarPartida
        MessageTemplate mtJugarPartida =
                MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
        addBehaviour( new TareaJugarPartida(this, mtJugarPartida));
    }
    
     @Override
    protected void takeDown() {
        //Desregistro de las Páginas Amarillas
        try {
            DFService.deregister(this);
	}
            catch (FIPAException fe) {
            fe.printStackTrace();
	}
        //Se liberan las suscripciones y se despide
        
//        for (AID policia : listaPolicias.keySet()) {
//            listaPolicias.get(policia).cancel(policia, true);
//        }
        System.out.println("Finaliza la ejecución de " + this.getName());
    }
    
    private Jugada calcularJugada( EntregarJugada info) {
        Jugada jugada;
        String ladronRival = "";
        
        Iterator it = info.getJugadores().iterator();
        while(it.hasNext()) {
            Jugador agenteJugador = (Jugador) it.next();
            if (this.getName().compareTo(agenteJugador.getAgenteJugador().getName()) != 0) {
                ladronRival = agenteJugador.getAgenteJugador().getName();
            }
        }
        
        if (traidores.contains(ladronRival)) {
            jugada = new Jugada(OntologiaDilemaPrisionero.HABLAR);
        } else {
            jugada = new Jugada(OntologiaDilemaPrisionero.CALLAR );
        }
        
        return jugada;
    }
    
    private void definirMiJugador() {
        jugador = new Jugador(NOMBRE_JUGADOR_IMPAR , getAID());
    }
    
    private void analizarResultadoJugada( ResultadoJugada resultado, ACLMessage cfp) {
        String ladronRival = "";
        Action ac = null;
        
        String idPartida = resultado.getPartida().getIdPartida();
        int penaRecompensa = RECOMPENSA;
        
        // Recogemos la información del mensaje del policía
        try {
            ac = (Action) manager.extractContent(cfp);
        } catch (Codec.CodecException | OntologyException ex) {
            Logger.getLogger(AgenteLadron.class.getName()).log(Level.SEVERE, null, ex);
        }
        EntregarJugada info = (EntregarJugada) ac.getAction();
        
        Iterator it = info.getJugadores().iterator();
        while(it.hasNext()) {
            Jugador agenteJugador = (Jugador) it.next();
            if (this.getName().compareTo(agenteJugador.getAgenteJugador().getName()) != 0) {
                ladronRival = agenteJugador.getAgenteJugador().getName();
            }
        }
        
        if ( resultado.getCondenaRecibida() != penaRecompensa) {
            traidores.add(ladronRival);
        }
    } 
    
    private void realizarSuscripcion( AID agentePolicia ) {
        InformarPartida infoPartida;
        ACLMessage msg;
        
        if ( listaPolicias.get(agentePolicia) == null ) {
            // Si no hemos contactado con el policía realizamos la suscripción
            infoPartida = new InformarPartida(jugador);
        
            //Creamos el mensaje para lanzar el protocolo Subscribe
            msg = new ACLMessage(ACLMessage.SUBSCRIBE);
            msg.setProtocol(FIPANames.InteractionProtocol.FIPA_SUBSCRIBE);
            msg.setSender(this.getAID());
            msg.setLanguage(codec.getName());
            msg.setOntology(ontology.getName());
            msg.addReceiver(agentePolicia);
        
            Action ac = new Action(this.getAID(), infoPartida);
            try {
                manager.fillContent(msg, ac);
            } catch (Codec.CodecException | OntologyException ex) {
                Logger.getLogger(AgenteLadronImpar.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            // Almacenamos la suscripción para su cancelación con la finalización
            // del agente
            TareaInformarPartida suscripcion = new TareaInformarPartida(this, msg);
            listaPolicias.put(agentePolicia, suscripcion);
            this.addBehaviour(suscripcion);
        }
    }
    
//    private void sumarPartidaPolicia( String nombrePolicia ) {
//  
//        Integer numPartidas = partidasPolicia.get( nombrePolicia );
//        if ( numPartidas != null) {
//            // Añadimos una nueva partida al policía
//            numPartidas = numPartidas + 1;
//            partidasPolicia.put(nombrePolicia, numPartidas);
//        } else {
//            // Es la primera
//            numPartidas = 1;
//            partidasPolicia.put(nombrePolicia, numPartidas);
//        }
//    }
//    
//    private void restarPartidaPolicia( String nombrePolicia ) {
//  
//        Integer numPartidas = partidasPolicia.get( nombrePolicia );
//        if ( numPartidas != null) {
//            // Restamos una partida al policía
//            numPartidas = numPartidas - 1;
//            if ( numPartidas > 0 ) {
//                partidasPolicia.put(nombrePolicia, numPartidas);
//            } else {
//                partidasPolicia.remove(nombrePolicia);
//            }
//            
//        }
//    }
//    
//    private boolean hayPartidasPolicia( String nombrePolicia ) {
//        
//        return partidasPolicia.get(nombrePolicia) != null;
//    }
    
    class TareaProponerPartida extends ProposeResponder {

        public TareaProponerPartida(Agent agente, MessageTemplate mt) {
            super(agente, mt);
        }

        @Override
        protected ACLMessage prepareResponse(ACLMessage propose) throws NotUnderstoodException, RefuseException {
            
            ACLMessage respuesta = propose.createReply();
            
            // Decidimos si aceptamos una nueva nuevaPartida
            if (partidasActivas < MAX_PARTIDAS) {
                // Contabilizamos las partidas activas y las que tenemos
                // con un agente Policia
                partidasActivas++;
                
                // Enviamos la respuesta de aceptación
                respuesta.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                
                // Creamos el contenido del mensaje
                ProponerPartida nuevaPartida = null;
                try {
                    Action ac = (Action) manager.extractContent(propose);
                    nuevaPartida = (ProponerPartida) ac.getAction();
                } catch (Codec.CodecException | OntologyException ex) {
                    Logger.getLogger(AgenteLadronImpar.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                Partida partida = nuevaPartida.getPartida();
                PartidaAceptada partidaAceptada = new PartidaAceptada(partida, jugador);
                partidas.put(nuevaPartida.getPartida().getIdPartida(), 
                                nuevaPartida.getCondiciones());
                
                try {
                    manager.fillContent(respuesta, partidaAceptada);
                } catch (Codec.CodecException | OntologyException ex) {
                    Logger.getLogger(AgenteLadronImpar.class.getName()).log(Level.SEVERE, null, ex);
                }
                
//                System.out.println(respuesta);
                
                realizarSuscripcion(propose.getSender());
                
                mensajesPendientes.add("Se acepta la partida: " + partida.getIdPartida() +
                        " , número de partidas activas: " + partidasActivas);
                return respuesta;
            } else {
                // Muchas partidas
                respuesta.setPerformative(ACLMessage.REJECT_PROPOSAL);
                
                mensajesPendientes.add("Se rechaza la partida, número de partidas activas: "
                    + partidasActivas);
                return respuesta;
            }
        }
    }
    
    class TareaJugarPartida extends ContractNetResponder {
        
        public TareaJugarPartida(Agent a, MessageTemplate mt) {
            super(a, mt);
        }

        @Override
        protected ACLMessage handleCfp(ACLMessage cfp) throws RefuseException, FailureException, NotUnderstoodException {
            ACLMessage respuesta;
            Action ac = null;
            
            // Recogemos la información del mensaje del policía
            try {
                ac = (Action) manager.extractContent(cfp);
            } catch (Codec.CodecException | OntologyException ex) {
                Logger.getLogger(AgenteLadronImpar.class.getName()).log(Level.SEVERE, null, ex);
            }
            EntregarJugada info = (EntregarJugada) ac.getAction();
            
            // Calculamos la jugada a realizar
            Jugada jugada = calcularJugada(info);
            
            // Creamos el menaje de respuesta con la jugada
            respuesta = cfp.createReply();
            respuesta.setSender(myAgent.getAID());
            respuesta.setPerformative(ACLMessage.PROPOSE);
            
            // Rellenamos el contenido del mensaje
            JugadaEntregada movimiento = new JugadaEntregada( info.getPartida(), jugador, jugada);
            
            try {
                manager.fillContent(respuesta, movimiento);
            } catch (Codec.CodecException | OntologyException ex) {
                Logger.getLogger(AgenteLadronImpar.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            mensajesPendientes.add("Movimiento para la partida: " + info.getPartida().getIdPartida()
                        + "\ncon la jugada: " + jugada.getRespuesta());
            
//            System.out.println(respuesta);
            return respuesta;
        }

        @Override
        protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose, ACLMessage accept) throws FailureException {
            ACLMessage respuesta;
            ResultadoJugada resultado = null;
            
            // Respondemos con un Inform para indicar que hemos recibido el resultado
            // de la jugada
            try {
                System.out.println(accept);
                resultado = (ResultadoJugada) manager.extractContent(accept);
            } catch (Codec.CodecException | OntologyException ex) {
                Logger.getLogger(AgenteLadronImpar.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            // Analizamos el resultado de la jugada para ajustar la estrategia
            analizarResultadoJugada(resultado, cfp);
            
            mensajesPendientes.add("En la partida: " + resultado.getPartida().getIdPartida()
                            + "\nhemos recibido una pena de: " + resultado.getCondenaRecibida()
                            + " años");
            
            respuesta = accept.createReply();
            respuesta.setPerformative(ACLMessage.INFORM);
            return respuesta;
        }
        
        
    } 
    
    class TareaInformarPartida extends SubscriptionInitiator {
        
        public TareaInformarPartida(Agent a, ACLMessage msg) {
            super(a, msg);
        }

        @Override
        protected void handleAgree(ACLMessage agree) {
            mensajesPendientes.add("Se ha realizado la suscripcion con el policía: "
                + agree.getSender().getLocalName());
        }

        @Override
        protected void handleInform(ACLMessage inform) {
            DetalleInforme informe = null;
            Error error;
            GanadorPartida ganador;
            //String nombrePolicia;
            
            // Recuperamos el ganador del mensaje
            try {
                informe = (DetalleInforme) manager.extractContent(inform);
            } catch (Codec.CodecException | OntologyException ex) {
                Logger.getLogger(AgenteLadronImpar.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            // Se ha finalizado la partida
            partidasActivas--;
            
            // Comprobamos el resultado el informe
            if (informe.getDetalle() instanceof GanadorPartida) {
                ganador = (GanadorPartida) informe.getDetalle();
                mensajesPendientes.add("Se ha finalizado la partida: " + informe.getPartida().getIdPartida()
                        + "\nque ha ganado el jugador: " + ganador.getJugador().getNombre() +
                        "\nel número de partidas activas es: " + partidasActivas);
            } else {
                error = (Error) informe.getDetalle();
                mensajesPendientes.add("Se ha producido un ERROR: " + error.getDetalle() +
                        "\nel número de partidas activas es: " + partidasActivas);
            }
            
            // Cancelamos la suscripcion y no esperamos respuesta
            // si no quedan partidas con el policia
            //cancel(inform.getSender(), true);
        }
    }
    
    class TareaBuscarConsola extends TickerBehaviour {
        //Se buscarán consolas 
        public TareaBuscarConsola(Agent a, long period) {
            super(a, period);
        }

        @Override
        protected void onTick() {
            //Busca agentes consola
            DFAgentDescription template = new DFAgentDescription();
            ServiceDescription sd = new ServiceDescription();
            sd.setName(OntologiaDilemaPrisionero.REGISTRO_CONSOLA);
            template.addServices(sd);
            try {
                DFAgentDescription[] result = DFService.search(myAgent, template); 
                if (result.length > 0) {
                    System.out.println("Se han encontrado las siguientes agentes consola:");
                    agentesConsola = new AID[result.length];
                    for (int i = 0; i < result.length; ++i) {
                        agentesConsola[i] = result[i].getName();
                        System.out.println(agentesConsola[i].getName());
                    }
                }
                else {
                    System.out.println("No se han encontrado agentes consola:");
                    agentesConsola = null;
                } 
            }
            catch (FIPAException fe) {
		fe.printStackTrace();
            }
        }
    }
    
    class TareaEnvioConsola extends TickerBehaviour {
        //Tarea de ejemplo que se repite cada 10 segundos
        public TareaEnvioConsola(Agent a, long period) {
            super(a, period);
        }

        @Override
        protected void onTick() {
            ACLMessage mensaje;
            if (agentesConsola != null) {
                AID consola = agentesConsola[PRIMERO];
                Iterator it = mensajesPendientes.iterator();
                while ( it.hasNext() ) {
                    System.out.println("Empieza el envío");
                    mensaje = new ACLMessage(ACLMessage.INFORM);
                    mensaje.setSender(myAgent.getAID());
                    mensaje.addReceiver(consola);
                    String contenido = (String) it.next();
           
                    mensaje.setContent(contenido);
            
                    System.out.println("Enviado a: " + consola.getName());
                    System.out.println("Contenido: " + mensaje.getContent());
            
                    myAgent.send(mensaje);
                    it.remove();
                }
            }
        }
    }
}
